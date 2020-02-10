package com.neoga.boltacution.security.controller;

import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.dto.SignupDto;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import com.neoga.boltacution.security.dto.LoginDto;
import com.neoga.boltacution.security.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequiredArgsConstructor
@Api(tags = {"auth API"})
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "로그인", notes = "로그인을 하며 jwt 토큰 발행")
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto.getEmail(), loginDto.getPasswd());
        return ResponseEntity.ok().body(token);
    }

}
