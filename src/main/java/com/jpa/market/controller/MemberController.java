package com.jpa.market.controller;

import com.jpa.market.dto.LoginRequestDto;
import com.jpa.market.dto.MemberJoinDto;
import com.jpa.market.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    // 회원가입 처리
    // @RequestBody: Body에 담겨있는 http 요청 정보를 java 객체로 변환
    @PostMapping("/join")
    public ResponseEntity<Long> join(
            @RequestBody @Valid MemberJoinDto dto
    ) {
        Long memberId = memberService.joinMember(dto);

        return ResponseEntity.ok(memberId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequestDto request,
            HttpServletRequest httpRequest
    ) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());

            // 인증을 수행
            // 매니저가 토큰을 넘겨받아서 DB에서 조회, 비밀번호를 비교하도록 함
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 인증에 성공하면 SecurityContext에 저장 (서버에서 인증된 해당 유저를 기억)
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 인증 정보를 유지할 수 있도록 세션을 생성
            HttpSession session = httpRequest.getSession(true);

            // 세션에 저장
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            // Response 바디에 내용을 담아 로그인 성공에 대한 정보를 프론트로 전달
            return ResponseEntity.ok().body(Map.of(
                    "message", "로그인 성공",
                    "loginId", authentication.getName(),
                    "role", authentication.getAuthorities()
                            .stream()
                            .map(a -> a.getAuthority())
                            .toList()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                    "message", "아이디 또는 비밀번호가 틀렸습니다."
            ));
        }
    }
}
