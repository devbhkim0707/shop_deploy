package com.jpa.market;

import com.jpa.market.dto.MemberJoinDto;
import com.jpa.market.entity.Cart;
import com.jpa.market.entity.Member;
import com.jpa.market.repository.CartRepository;
import com.jpa.market.repository.MemberRepository;
import com.jpa.market.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CartTest {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @PersistenceContext
    EntityManager em;

    public MemberJoinDto createMember() {
        MemberJoinDto dto = new MemberJoinDto();

        dto.setLoginId("test1");
        dto.setPassword("1234");
        dto.setName("김자바");
        dto.setEmail("test1@naver.com");
        dto.setAddress("부산시 연제구");

        return dto;
    }


    @Test
    public void findCartAndMemberTest() {
        // 회원 정보 생성
        MemberJoinDto dto = createMember();

        // 회원 가입 실행
        Long savedMemberId = memberService.joinMember(dto);

        // 회원 아이디 값으로 저장된 회원 조회
        Member member = memberRepository.findById(savedMemberId)
                .orElseThrow(EntityNotFoundException::new);

        // 조회한 회원 정보로 장바구니 생성
        Cart cart = Cart.createCart(member);
        cartRepository.save(cart);

        // 영속성 컨텍스트 반영
        em.flush();
        em.clear();

        System.out.println("로딩 확인");

        // 장바구니 정보 조회
        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        // 조회한 장바구니 정보의 회원 정보와 가입한 회원의 아이디가 같은지 확인
        assertThat(savedMemberId).isEqualTo(savedCart.getMember().getId());
    }



}
