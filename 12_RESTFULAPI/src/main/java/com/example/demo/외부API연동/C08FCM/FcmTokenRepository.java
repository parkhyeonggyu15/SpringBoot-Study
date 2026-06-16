package com.example.demo.외부API연동.C08FCM;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
 * FCM 토큰 저장소 (Spring Data JPA)
 *  - JpaRepository<FcmToken, Long> 상속으로 save/findAll/delete 등 기본 CRUD 자동 제공
 *  - 아래는 메서드 이름 규칙으로 자동 생성되는 쿼리 메서드
 */
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    Optional<FcmToken> findByToken(String token);   // 토큰 값으로 단건 조회

    List<FcmToken> findByUsername(String username); // 특정 사용자의 토큰 목록 조회

    boolean existsByToken(String token);            // 토큰 중복 여부 확인 (등록 전 체크용)

    void deleteByToken(String token);               // 토큰 값으로 삭제 (만료/해지 시)

}