package com.neoga.boltacution.security.controller;


import com.neoga.boltacution.security.dto.RetKakaoAuth;
import com.neoga.boltacution.security.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/social")
public class SocialContoller {
    private final Environment env;
    private final SocialService socialLoginService;
    @Value("${kakao.loginURL}")
    private String kakaoLoginURL;
    @Value("${base.URL}")
    private String baseUrl;
    @Value("${kakao.clientId}")
    private String kakaoClientId;
    @Value("${kakao.redirectURI}")
    private String kakaoRedirectURI;

    //카카오 로그인 페이지 주소 반환
    @GetMapping("/kakao/login")
    public String socialLogin() {
        StringBuilder loginUrl = new StringBuilder()
                .append(kakaoLoginURL)
                .append("?client_id=").append(kakaoClientId)
                .append("&response_type=code")
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirectURI);
        return loginUrl.toString();
    }

    //카카오 인증 후 리다이렉션 경로 토큰 반환
    @GetMapping(value = "/kakao")
    public RetKakaoAuth redirectKakao(@RequestParam String code) {
        RetKakaoAuth token = socialLoginService.getKakaoToken(code);
        return token;
    }
}