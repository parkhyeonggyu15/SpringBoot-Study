package com.example.demo.외부API연동.C08FCM;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fcm_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(length = 500, nullable = false, unique = true)
    private String token;

    private LocalDateTime registeredAt;

    @PrePersist
    public void onCreate() {
        this.registeredAt = LocalDateTime.now();
    }
}
