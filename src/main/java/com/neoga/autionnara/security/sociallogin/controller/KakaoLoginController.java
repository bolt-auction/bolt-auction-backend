package com.neoga.autionnara.security.sociallogin.controller;

import com.neoga.autionnara.security.sociallogin.service.KakaoLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class KakaoLoginController {
    @Autowired
    KakaoLoginService kakaoLoginService;

    @GetMapping("/kakao")
    public String loginKakao(@RequestParam("code") String code){
        String token = kakaoLoginService.getAccessToken(code);
        return token;
    }
}
