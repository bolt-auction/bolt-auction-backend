package com.neoga.boltacution.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class SocialLoginService {
    @Autowired
    Environment env;

    @Autowired
    RestTemplate restTemplate;

    public String getAccessToken (String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", env.getProperty("kakao.clientId"));
        map.add("redirect_uri", env.getProperty("kakao.redirectURI"));
        map.add("code", code);

        String result = restTemplate.postForObject(env.getProperty("kakao.tokenURL"), map, String.class);

        return result;
    }
}


