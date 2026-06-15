package com.example.demo.외부API연동.C08FCM;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
@Slf4j
public class FcmTokenController {

    private final FcmTokenRepository fcmTokenRepository;

    @PostMapping("/token")
    public ResponseEntity<?> saveToken(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String token = payload.get("token");

        log.info("POST /api/fcm/token....." + username + " token = " + token);
        if (!fcmTokenRepository.existsByToken(token)) {
            fcmTokenRepository.save(FcmToken.builder()
                    .username(username)
                    .token(token)
                    .build());
        }

        return ResponseEntity.ok().build();
    }
}