package com.example.demo.외부API연동.C08FCM;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Cloudflare 빠른 터널(quick tunnel) 생성용
 * - 버튼 클릭 → cloudflared 실행 → https://*.trycloudflare.com 주소 반환
 * - HTTPS 라서 폰 등 외부기기에서도 FCM 웹푸시 테스트 가능 (웹푸시는 HTTPS/localhost 에서만 동작)
 * [문서] Cloudflare Quick Tunnels:
 *   https://developers.cloudflare.com/cloudflare-one/connections/connect-networks/do-more-with-tunnels/trycloudflare/
 * 주의: 로컬 서버를 공개 인터넷에 노출함 (테스트용, 프로덕션 금지)
 */
@RestController
@RequestMapping("/api/fcm")
@Slf4j
public class FcmTunnelController {

    // ----- 상태/상수 -----
    // cloudflared 출력 로그에서 공개 URL(https://*.trycloudflare.com)을 뽑아내는 정규식
    private static final Pattern URL_PATTERN =
            Pattern.compile("https://[a-z0-9-]+\\.trycloudflare\\.com");

    private Process tunnelProcess;          // 실행 중인 cloudflared 프로세스 핸들
    private volatile String tunnelUrl;      // 생성된 공개 URL (멀티스레드 가시성 위해 volatile)

    // =========================================================================
    // [기능] 터널 생성 — 버튼 클릭 시 호출 (POST /api/fcm/tunnel)
    //  처리 흐름: 재사용 체크 → cloudflared 실행 → 출력에서 URL 파싱 → 반환
    // =========================================================================
    @PostMapping("/tunnel")
    public ResponseEntity<String> createTunnel() {
        // 1) 이미 살아있는 터널이 있으면 새로 만들지 않고 그대로 재사용
        if (tunnelUrl != null && tunnelProcess != null && tunnelProcess.isAlive()) {
            return ResponseEntity.ok(tunnelUrl);
        }

        try {
            // 2) cloudflared 프로세스 실행 (로컬 8080 을 외부에 노출)
            ProcessBuilder pb = new ProcessBuilder(
                    "cloudflared", "tunnel", "--url", "http://localhost:8080");
            pb.redirectErrorStream(true);   // stderr(로그)도 같이 읽기
            tunnelProcess = pb.start();

            // 3) 프로세스 출력 스트림 준비 (URL 로그를 읽어내기 위함)
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(tunnelProcess.getInputStream(), StandardCharsets.UTF_8));

            // 4) 출력에서 trycloudflare 주소가 나올 때까지 대기(최대 30초)
            long deadline = System.currentTimeMillis() + 30_000;
            String line;
            while (System.currentTimeMillis() < deadline && (line = reader.readLine()) != null) {
                Matcher m = URL_PATTERN.matcher(line);
                if (m.find()) {
                    // 4-1) URL 발견 → 저장 후 즉시 반환
                    tunnelUrl = m.group();
                    log.info("cloudflare tunnel ready: {}", tunnelUrl);
                    drainAsync(reader);     // 남은 출력은 계속 비워줌(버퍼 막힘 방지)
                    return ResponseEntity.ok(tunnelUrl);
                }
            }
            // 4-2) 제한 시간 내 URL 을 못 찾은 경우
            return ResponseEntity.status(500).body("터널 URL을 가져오지 못했습니다. (cloudflared 출력 확인)");

        } catch (Exception e) {
            // 5) cloudflared 미설치/PATH 오류 등 실행 실패 처리
            log.error("터널 생성 실패", e);
            return ResponseEntity.status(500)
                    .body("터널 생성 실패: " + e.getMessage() + " (cloudflared 설치/PATH 확인)");
        }
    }

    // =========================================================================
    // [보조] 출력 버퍼 비우기
    //  - cloudflared 의 이후 출력을 데몬 스레드로 계속 읽어 버려
    //    출력 버퍼가 가득 차 프로세스가 멈추는 것을 방지
    // =========================================================================
    private void drainAsync(BufferedReader reader) {
        Thread t = new Thread(() -> {
            try {
                while (reader.readLine() != null) { /* discard */ }
            } catch (Exception ignored) {
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
