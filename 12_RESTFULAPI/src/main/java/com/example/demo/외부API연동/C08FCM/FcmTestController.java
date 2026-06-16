package com.example.demo.외부API연동.C08FCM;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/*
 * FCM 발송 컨트롤러
 * send.html -> 이 컨트롤러 -> FcmService -> Firebase -> receive.html
 * [문서] Admin SDK로 토큰에 메시지 발송: https://firebase.google.com/docs/cloud-messaging/send/admin-sdk
 *        (토큰 등록은 FcmTokenController, 수신은 receive.html 참고)
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
@Slf4j
public class FcmTestController {

    private final FcmTokenRepository fcmTokenRepository;   // 등록된 토큰 조회
    private final FcmService fcmService;                   // 실제 FCM 발송 로직

    // =========================================================================
    // [기능] 전체 발송 (POST /api/fcm/send)
    //  - send.html 에서 입력한 제목/내용을 등록된 모든 토큰으로 일괄 발송
    //  - 토큰별 성공/실패를 집계해 결과 문자열로 반환
    // =========================================================================
    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody Map<String, String> payload) {
        // 1) 요청 본문에서 제목/내용 추출
        String title = payload.get("title");
        String body  = payload.get("body");
        log.info("POST /api/fcm/send.... title=" + title);

        // 2) 발송 대상 토큰 전체 조회 — 없으면 안내 후 종료
        List<FcmToken> tokens = fcmTokenRepository.findAll();
        if (tokens.isEmpty()) {
            return ResponseEntity.ok("등록된 토큰이 없습니다. 먼저 수신 페이지(/fcm/receive)를 여세요.");
        }

        // 3) 토큰마다 발송 시도 — 개별 실패가 전체를 중단시키지 않도록 try/catch
        int success = 0, fail = 0;
        for (FcmToken token : tokens) {
            try {
                fcmService.sendFcmMessage(token.getToken(), title, body);
                success++;
            } catch (Exception e) {
                fail++;
                System.out.println("발송 실패: " + e.getMessage());
            }
        }

        // 4) 성공/실패 집계 결과 반환
        return ResponseEntity.ok("발송 완료 - 성공 " + success + ", 실패 " + fail);
    }

}

