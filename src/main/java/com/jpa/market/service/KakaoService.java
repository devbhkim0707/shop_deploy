package com.jpa.market.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpa.market.dto.KakaoTokenDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class KakaoService {

    public KakaoTokenDto getKakaoAccessToken(String code) {
        // 처리에 필요한 Url 주소
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        // 스프링에서 제공하는 객체로 브라우저 없이 http요청을 처리할 수 있음
        RestTemplate rt = new RestTemplate();

        // httpHeaders (http 요청 헤더 생성)
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // httpBody 생성 (전달해야 하는 데이터를 추가)
        // MultiValueMap: 값을 리스트 형태로 저장 (모든 값을 전부 저장)
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "3fe9ed51ecff1ff4d568313a7b233713");
        params.add("client_secret", "DGKYssQGpb9swdL9LQJtFHserkI38dwj");
        params.add("redirect_uri", "http://localhost:8000/auth/members/kakao");
        params.add("code", code);

        // header와 body를 하나로 합침
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        // 실제 요청 보내기
        ResponseEntity<String> response = rt.exchange(
                reqUrl,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // json과 java의 변환기
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(response.getBody(), KakaoTokenDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 토큰 가져오기 실패", e);
        }

        // 전체 응답 리턴
//        return response.getBody();

    }

    public String getKakaoUserInfo(KakaoTokenDto tokenDto) {
        // 처리에 필요한 Url 주소
        String reqUrl = "https://kapi.kakao.com/v2/user/me";

        // 스프링에서 제공하는 객체로 브라우저 없이 http요청을 처리할 수 있음
        RestTemplate rt = new RestTemplate();

        // httpHeaders (http 요청 헤더 생성)
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", "Bearer " + tokenDto.getAccess_token());
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // header와 body를 하나로 합침
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);

        // 실제 요청 보내기
        ResponseEntity<String> response = rt.exchange(
                reqUrl,
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        // 전체 응답 리턴
        return response.getBody();

    }
}
