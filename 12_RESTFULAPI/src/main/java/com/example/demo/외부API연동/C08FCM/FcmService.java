package com.example.demo.외부API연동.C08FCM;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

/*
 * FCM 메시지 발송 (Firebase Admin SDK)
 * [문서] Admin SDK로 기기 토큰에 발송:
 *   https://firebase.google.com/docs/cloud-messaging/send/admin-sdk
 */
@Service
public class FcmService {

    // =========================================================================
    // [기능] 단일 기기 발송
    //  - 특정 기기 토큰으로 알림(title/body) 발송 → 발송 성공 시 메시지 ID 반환
    //  - 호출처: FcmTestController(등록된 모든 토큰에 반복 호출)
    // =========================================================================
    public String sendFcmMessage(String targetToken, String title, String body) throws FirebaseMessagingException {
        // 1) 메시지 조립 — 받는 기기(token) + 표시 알림(notification)
        Message message = Message.builder()
                .setToken(targetToken)                       // 받는 대상 기기 토큰
                .setNotification(Notification.builder()
                        .setTitle(title)                     // 알림 제목
                        .setBody(body)                       // 알림 내용
                        .build())
                .build();

        // 2) Firebase 서버로 전송 (동기) → 발급된 메시지 ID 반환
        return FirebaseMessaging.getInstance().send(message);
    }
}