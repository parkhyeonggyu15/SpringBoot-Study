package com.example.demo.외부API연동.C08FCM;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface  FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByToken(String token);

    List<FcmToken> findByUsername(String username);

    boolean existsByToken(String token);

    void deleteByToken(String token);
}
