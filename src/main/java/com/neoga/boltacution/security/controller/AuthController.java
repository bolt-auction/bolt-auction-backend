package com.neoga.boltacution.security.controller;

import com.neoga.boltacution.security.dto.LoginDto;
import com.neoga.boltacution.security.dto.LoginUserDto;
import com.neoga.boltacution.security.service.AuthService;
import com.neoga.boltacution.security.service.SocialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
    private final SocialService kakaoLoginService;
    @ApiOperation(value = "로그인", notes = "로그인을 하며 jwt 토큰 발행")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        LoginUserDto loginUserDetail = authService.login(loginDto);

        EntityModel<LoginUserDto> entityModel = new EntityModel(loginUserDetail);
        entityModel.add(linkTo(methodOn(AuthController.class).login(loginDto)).withSelfRel());
        entityModel.add(new Link("/swagger-ui.html#/auth%20API/loginUsingPOST").withRel("profile"));
        return ResponseEntity.ok().body(entityModel);
    }
}
