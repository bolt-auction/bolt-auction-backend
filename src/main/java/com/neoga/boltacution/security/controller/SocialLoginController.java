package com.neoga.boltacution.security.controller;

import com.neoga.boltacution.security.service.SocialLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class SocialLoginController {
    @Autowired
    SocialLoginService kakaoLoginService;

    @GetMapping("/kakao/login")
    public String loginKakao(@RequestParam("code") String code){
        return kakaoLoginService.getAccessToken(code);
    }
}
