package com.neoga.boltacution.security.controller;

import com.neoga.boltacution.exception.custom.CEmailLoginFailedException;
import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.domain.Role;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import com.neoga.boltacution.security.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequiredArgsConstructor
@Api(tags = {"auth API"})
@RestController
@RequestMapping(value = "/api")
public class AuthController {

    private final AuthService authService;
    private final MemberRepository memberRepo;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "로그인을 하며 jwt 토큰 발행")
    @PostMapping(value = "/login")
    public ResponseEntity login(@ApiParam(value = "회원아이디", required = true)@RequestParam String email,
                                @ApiParam(value = "비밀번호", required = true) @RequestParam String passwd) {
        String token = authService.login(email, passwd);
        return ResponseEntity.ok().body(token);
    }

    @ApiOperation(value = "회원가입", notes = "정보를 입력받아 회원가입")
    @PostMapping(value = "/signup")
    public ResponseEntity signin(@ApiParam(value = "회원아이디", required = true)@RequestParam String email,
                                 @ApiParam(value = "비밀번호", required = true)@RequestParam String passwd,
                                 @ApiParam(value = "닉네임", required = true)@RequestParam String name) {
        Members newMember = Members.builder()
                .email(email)
                .passwd(passwordEncoder.encode(passwd))
                .name(name)
                .role(Collections.singletonList(Role.USER))
                .build();
        memberRepo.save(newMember);
        return ResponseEntity.ok().build();
    }
}
