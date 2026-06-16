package com.example.demo.외부API연동.C08FCM;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/*
 * FCM 기기 토큰 엔티티 (테이블: fcm_tokens)
 *  - 발송 대상이 되는 기기 토큰을 사용자명과 함께 저장
 */
@Entity
@Table(name = "fcm_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FcmToken {

    // ----- 컬럼 정의 -----
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                              // PK (자동 증가)

    @Column(nullable = false)
    private String username;                      // 토큰 소유 사용자명

    @Column(length = 500, nullable = false, unique = true)
    private String token;                         // FCM 기기 토큰 (중복 불가)

    private LocalDateTime registeredAt;          // 등록 시각

    // ----- 생명주기 콜백 -----
    // INSERT 직전 자동 실행 → 등록 시각을 현재 시간으로 설정
    @PrePersist
    public void onCreate() {
        this.registeredAt = LocalDateTime.now();
    }
}
