package com.neoga.boltauction.memberstore.member.controller;

import com.neoga.boltauction.exception.custom.CMemberExistException;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.dto.SignupRequestDto;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import com.neoga.boltauction.memberstore.member.service.MemberService;
import com.neoga.boltauction.security.dto.KakaoProfile;
import com.neoga.boltauction.security.dto.LoginResponseDto;
import com.neoga.boltauction.security.service.AuthService;
import com.neoga.boltauction.security.service.KakaoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;
    private final KakaoService kakaoService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "자신 정보조회", notes = "아직 미완성 (반환 메시지는 수정계획)")
    @GetMapping()
    public ResponseEntity findMemberById(){
        Long member_id = authService.getLoginInfo().getMemberId();
        Members findMember = memberService.findMemberById(member_id);

        Resource<Members> entityModel = new Resource(findMember);
        entityModel.add(linkTo(MemberController.class).withSelfRel());
        entityModel.add(new Link("/member-controller/findMemberByIdUsingPUT").withRel("profile"));

        return ResponseEntity.ok().body(entityModel);
    }

    @ApiOperation(value = "회원가입", notes = "정보를 입력받아 회원가입 (반환 메시지는 수정계획)")
    @PostMapping()
    public ResponseEntity signup(@RequestBody SignupRequestDto signupRequest) {
        Members newMember = memberService.saveMember(signupRequest);

        Resource<LoginResponseDto> entityModel = new Resource(newMember);
        entityModel.add(linkTo(methodOn(MemberController.class).signup(signupRequest)).withSelfRel());
        entityModel.add(new Link("/swagger-ui.html#/auth%20API/loginUsingPOST").withRel("profile"));

        return ResponseEntity.ok().body(entityModel);
    }

    @ApiOperation(value = "소셜 회원가입", notes = "소셜 계정 회원가입을 한다.")
    @PostMapping(value = "/social/{provider}")
    public ResponseEntity signupSocial(@ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
                                       @ApiParam(value = "소셜 access_token", required = true) @RequestParam String accessToken,
                                       @ApiParam(value = "닉네임", required = true) @RequestParam String name) {

        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        Optional<Members> member = memberRepository.findByUidAndProvider(String.valueOf(profile.getId()), provider);
        if(member.isPresent())
            throw new CMemberExistException();

        memberRepository.save(Members.builder()
                .uid(String.valueOf(profile.getId()))
                .provider(provider)
                .name(name)
                .role(Collections.singletonList("USER"))
                .build());

        return ResponseEntity.ok().build();
    }
}
