package com.neoga.boltauction.memberstore.member.controller;

import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.dto.SignupRequestDto;
import com.neoga.boltauction.memberstore.member.service.MemberService;
import com.neoga.boltauction.security.dto.LoginResponseDto;
import com.neoga.boltauction.security.service.AuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    @ApiOperation(value = "자신 정보조회")
    @GetMapping()
    public ResponseEntity findMemberById(){
        Long member_id = authService.getLoginInfo().getMemberId();
        Members findMember = memberService.findMemberById(member_id);

        Resource<Members> entityModel = new Resource(findMember);
        entityModel.add(linkTo(MemberController.class).withSelfRel());
        entityModel.add(new Link("/member-controller/findMemberByIdUsingPUT").withRel("profile"));

        return ResponseEntity.ok().body(entityModel);
    }

    @ApiOperation(value = "회원가입", notes = "정보를 입력받아 회원가입 \n" +
            "403(Forbidden) - 이미 사용자가 있는 중복된 아이디 오류")
    @PostMapping()
    public ResponseEntity signup(@RequestBody SignupRequestDto signupRequest) {
        Members newMember = memberService.saveMember(signupRequest);

        Resource<LoginResponseDto> entityModel = new Resource(newMember);
        entityModel.add(linkTo(methodOn(MemberController.class).signup(signupRequest)).withSelfRel());
        entityModel.add(new Link("/swagger-ui.html#/auth%20API/loginUsingPOST").withRel("profile"));

        return ResponseEntity.ok().body(entityModel);
    }

    @ApiOperation(value = "소셜 회원가입", notes = "소셜 계정 회원가입을 한다. \n" +
            "403(Forbidden) - 이미 소셜 회원가입한 사용자")
    @PostMapping(value = "/social/{provider}")
    public ResponseEntity signupSocial(@ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
                                       @ApiParam(value = "소셜 access_token", required = true) @RequestParam String accessToken,
                                       @ApiParam(value = "닉네임", required = true) @RequestParam String name) {
        Members newMember = memberService.saveSocialMember(provider,accessToken,name);

        Resource<LoginResponseDto> entityModel = new Resource(newMember);
        entityModel.add(linkTo(methodOn(MemberController.class).signupSocial(provider,accessToken,name)).withSelfRel());
        entityModel.add(new Link("/swagger-ui.html#/auth%20API/loginByProviderUsingPOST").withRel("profile"));

        return ResponseEntity.ok().build();
    }
}
