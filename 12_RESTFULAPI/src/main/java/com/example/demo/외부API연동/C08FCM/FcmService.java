package com.example.demo.외부API연동.C08FCM;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

    public String sendFcmMessage(String targetToken, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        return FirebaseMessaging.getInstance().send(message);
    }
}
