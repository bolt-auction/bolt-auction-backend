package com.neoga.boltacution.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class SocialLoginService {
    @Value("${kakao.clientId}")
    private String kakaoClientId;
    @Value("${kakao.redirectURI}")
    private String kakaoRedirectURI;
    @Value("${kakao.tokenURL}")
    private String kakaoTokenURL;
    @Value("${base.URL}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public String getKakaoToken(String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", kakaoClientId);
        map.add("redirect_uri", baseUrl+kakaoRedirectURI);
        map.add("code", code);

        String token = restTemplate.postForObject(kakaoTokenURL, map, String.class);

        return token;
    }
}


