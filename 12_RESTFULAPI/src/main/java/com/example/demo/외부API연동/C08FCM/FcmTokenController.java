package com.example.demo.외부API연동.C08FCM;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*
 * FCM 등록 토큰 저장 (receive.html 에서 발급한 기기 토큰을 서버 DB에 등록)
 * [문서] 등록 토큰 관리 모범사례(저장·갱신·만료): https://firebase.google.com/docs/cloud-messaging/manage-tokens
 */
@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
@Slf4j
public class FcmTokenController {

    private final FcmTokenRepository fcmTokenRepository;   // 토큰 영속화(저장/조회) DAO

    // =========================================================================
    // [기능] 기기 토큰 등록 (POST /api/fcm/token)
    //  - receive.html 에서 발급한 토큰을 DB(fcm_tokens)에 저장
    //  - 같은 토큰 중복 저장 방지 (existsByToken 체크)
    // =========================================================================
    @PostMapping("/token")
    public ResponseEntity<?> saveToken(@RequestBody Map<String, String> payload) {
        // 1) 요청 본문에서 사용자명/토큰 추출
        String username = payload.get("username");
        String token = payload.get("token");

        log.info("POST /api/fcm/token....." + username + " token = " + token);

        // 2) 신규 토큰일 때만 저장 (중복 등록 방지)
        if (!fcmTokenRepository.existsByToken(token)) {
            fcmTokenRepository.save(FcmToken.builder()
                    .username(username)
                    .token(token)
                    .build());
        }

        // 3) 등록 완료 응답
        return ResponseEntity.ok().build();
    }
}