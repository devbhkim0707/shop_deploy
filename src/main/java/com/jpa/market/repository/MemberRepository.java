package com.jpa.market.repository;

import com.jpa.market.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // id 중복검사
    boolean existsByLoginId(String loginId);

    // 기존 가입자인지 이메일 중복 확인
    boolean existsByEmail(String email);

    // 로그인 처리(id만 전달, 비밀번호는 시큐리티가 알아서 처리함)
    // Optional: null이 들어올 수도 있음을 타입으로 강제적으로 지정
    Optional<Member> findByLoginId(String loginId);


}
