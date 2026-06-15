package com.example.demo.외부API연동.C08FCM;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class FirebaseInitializer {

    @PostConstruct
    public void initialize() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/firebase-adminsdk.json"); // 다운로드한 JSON 경로

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
