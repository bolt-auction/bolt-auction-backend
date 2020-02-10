package com.neoga.boltacution.memberstore.member.controller;

import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.dto.SignupDto;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import com.neoga.boltacution.memberstore.member.service.MemberService;
import com.neoga.boltacution.security.service.JwtTokenService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원정보조회", notes = "아직 미완성")
    @PutMapping()
    public ResponseEntity findMemberById(){
        Long member_id = jwtTokenService.getLoginId();
        Members findMember = memberService.findMemberById(member_id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원가입", notes = "정보를 입력받아 회원가입")
    @PostMapping(value = "/signup")
    public ResponseEntity signin(@RequestBody SignupDto signupDto) {
        Members newMember = memberService.saveMember(signupDto);
        return ResponseEntity.ok().build();
    }
}
