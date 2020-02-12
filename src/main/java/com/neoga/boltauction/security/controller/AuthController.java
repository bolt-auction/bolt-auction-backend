package com.neoga.boltauction.security.controller;

import com.neoga.boltauction.exception.custom.CEmailLoginFailedException;
import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import com.neoga.boltauction.security.dto.KakaoProfile;
import com.neoga.boltauction.security.dto.LoginDto;
import com.neoga.boltauction.security.dto.LoginUserDto;
import com.neoga.boltauction.security.service.AuthService;
import com.neoga.boltauction.security.service.JwtTokenService;
import com.neoga.boltauction.security.service.KakaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RequiredArgsConstructor
@Api(tags = {"auth API"})
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final AuthService authService;
    private final KakaoService kakaoService;
    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;

    @ApiOperation(value = "일반 로그인", notes = "로그인을 하며 jwt 토큰 발행")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        LoginUserDto loginUserDetail = authService.login(loginDto);

        EntityModel<LoginUserDto> entityModel = new EntityModel(loginUserDetail);
        entityModel.add(linkTo(methodOn(AuthController.class).login(loginDto)).withSelfRel());
        entityModel.add(new Link("/swagger-ui.html#/auth%20API/loginUsingPOST").withRel("profile"));

        return ResponseEntity.ok().body(entityModel);
    }

    @ApiOperation(value = "소셜 로그인", notes = "소셜 회원 로그인을 한다.")
    @PostMapping(value = "/login/social/{provider}")
    public ResponseEntity loginByProvider(
            @ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
            @ApiParam(value = "소셜 access_token", required = true) @RequestParam String accessToken) {
        LoginUserDto loginUserDetail = authService.socialLogin(accessToken, provider);

        EntityModel<LoginUserDto> entityModel = new EntityModel(loginUserDetail);
        entityModel.add(linkTo(methodOn(AuthController.class).loginByProvider(provider, accessToken)).withSelfRel());
        entityModel.add(new Link("/swagger-ui.html#/auth%20API/signinByProviderUsingPOST").withRel("profile"));

        return ResponseEntity.ok().body(entityModel);
    }

}
