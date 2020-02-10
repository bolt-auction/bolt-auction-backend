package com.neoga.boltacution.security.controller;

import com.neoga.boltacution.security.dto.LoginDto;
import com.neoga.boltacution.security.dto.LoginUserDetailDto;
import com.neoga.boltacution.security.service.AuthService;
import com.neoga.boltacution.security.service.SocialLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Api(tags = {"auth API"})
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final AuthService authService;
    private final SocialLoginService kakaoLoginService;
    @ApiOperation(value = "로그인", notes = "로그인을 하며 jwt 토큰 발행")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        LoginUserDetailDto loginUserDetail = authService.login(loginDto.getEmail(), loginDto.getPasswd());

//        LoginEntityModel entityModel = new LoginEntityModel(loginUserDetail);
//        entityModel.add(linkTo(methodOn(AuthController.class).login(loginDto)).withSelfRel());
//        entityModel.add(linkTo("http://localhost:8010/swagger-ui.html#/auth%20API/loginUsingPOST").withRel("profile"));
        return ResponseEntity.ok().body(loginUserDetail);
    }

    @ApiOperation(value = "카카오 로그인", notes = "미완성")
    @GetMapping("/kakao/login")
    public String loginKakao(@RequestParam("code") String code){
        return kakaoLoginService.getAccessToken(code);
    }
}
