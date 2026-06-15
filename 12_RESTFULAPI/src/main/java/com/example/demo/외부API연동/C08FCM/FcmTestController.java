package com.example.demo.외부API연동.C08FCM;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmTestController {


    private final FcmTokenRepository fcmTokenRepository;
    private final FcmService fcmService;

    @PostMapping("/test")
    public ResponseEntity<String> sendTestFcm() {
        List<FcmToken> tokens = fcmTokenRepository.findAll();

        for (FcmToken token : tokens) {
            String title = "테스트 알림";
            String body = "FCM 테스트 메시지입니다.";
            try {
                String response = fcmService.sendFcmMessage(token.getToken(), title, body);
                System.out.println("알림 전송 완료: " + response);
            } catch (Exception e) {
                System.out.println("알림 전송 실패: " + e.getMessage());
            }
        }

        return ResponseEntity.ok("테스트 FCM 알림 발송 완료");
    }

}