package com.example.demo.외부API연동.C08FCM;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

/*
 * Firebase Admin SDK 초기화 (앱 기동 시 1회)
 * [문서] Admin SDK 서버 설정: https://firebase.google.com/docs/admin/setup
 *
 * 키 파일 규칙
 *  - firebase-adminsdk.json          : 실제 서비스계정 키 (비밀, .gitignore 처리, 앱이 읽는 파일)
 *  - firebase-adminsdk.json.example  : 커밋용 예시 템플릿 (가짜값) — 콘솔에서 받은 키로 위 .json 만들기
 */
@Component
public class FirebaseInitializer {

    // =========================================================================
    // [기능] Firebase Admin SDK 초기화
    //  - @PostConstruct : 스프링 빈 생성 직후 1회 실행 (앱 기동 시점)
    //  - 이 초기화가 끝나야 FcmService 의 FirebaseMessaging 발송이 동작함
    // =========================================================================
    @PostConstruct
    public void initialize() throws IOException {
        // 1) 서비스계정 키 파일 로드 (Firebase 콘솔에서 발급받은 비밀 키)
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/firebase-adminsdk.json"); // 콘솔에서 받은 실제 키

        // 2) 키를 자격증명(Credentials)으로 변환하여 FirebaseOptions 구성
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // 3) 중복 초기화 방지 — 이미 초기화된 FirebaseApp 이 없을 때만 등록
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }


}