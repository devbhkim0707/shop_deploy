package com.jpa.market.entity;

import com.jpa.market.constant.OAuthType;
import com.jpa.market.constant.Role;
import com.jpa.market.dto.MemberJoinDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@ToString
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id", unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 50, unique = true)
    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuthType oauthType;

    @OneToOne(mappedBy = "member")
    private Cart cart;

    public static Member createMember(MemberJoinDto memberJoinDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.loginId = memberJoinDto.getLoginId();
        member.password = passwordEncoder.encode(memberJoinDto.getPassword());
        member.name = memberJoinDto.getName();
        member.email = memberJoinDto.getEmail();
        member.address = memberJoinDto.getAddress();
        member.role = Role.USER;
//        member.role = Role.ADMIN;
        member.oauthType = OAuthType.SHOP;

        return member;
    }

    public static Member createOAuthMember(
            String loginId,
            String nickname,
            String email,
            String password,
            OAuthType oauthType
    ) {
        Member member = new Member();
        member.loginId = loginId;
        member.password = password;
        member.name = nickname;
        member.email = email;
        member.role = Role.USER;
        member.oauthType = oauthType;

        return member;
    }
}
