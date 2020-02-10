package com.neoga.boltacution.memberstore.member.controller;

import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.dto.SignupDto;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberRepository memberRepo;
    private final PasswordEncoder passwordEncoder;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping()
    public ResponseEntity updateMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String member = authentication.getName();
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원가입", notes = "정보를 입력받아 회원가입")
    @PostMapping(value = "/signup")
    public ResponseEntity signin(@RequestBody SignupDto signupDto) {
        Members newMember = Members.builder()
                .email(signupDto.getEmail())
                .passwd(passwordEncoder.encode(signupDto.getPasswd()))
                .name(signupDto.getName())
                .role(Collections.singletonList("USER"))
                .build();
        memberRepo.save(newMember);
        return ResponseEntity.ok().build();
    }
}
