package com.neoga.boltacution.memberstore.member.controller;

import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.dto.SignupDto;
import com.neoga.boltacution.memberstore.member.service.MemberService;
import com.neoga.boltacution.security.dto.LoginUserDto;
import com.neoga.boltacution.security.service.JwtTokenService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "자신 정보조회", notes = "아직 미완성 (반환 메시지는 수정계획)")
    @GetMapping()
    public ResponseEntity findMemberById(){
        Long member_id = jwtTokenService.getLoginInfo().getMember_id();
        Members findMember = memberService.findMemberById(member_id);

        EntityModel<Members> entityModel = new EntityModel(findMember);
        entityModel.add(linkTo(MemberController.class).withSelfRel());
        entityModel.add(new Link("/member-controller/findMemberByIdUsingPUT").withRel("profile"));

        return ResponseEntity.ok().body(entityModel);
    }

    @ApiOperation(value = "회원가입", notes = "정보를 입력받아 회원가입 (반환 메시지는 수정계획)")
    @PostMapping(value = "/signup")
    public ResponseEntity signin(@RequestBody SignupDto signupDto) {
        Members newMember = memberService.saveMember(signupDto);

        EntityModel<LoginUserDto> entityModel = new EntityModel(newMember);
        entityModel.add(linkTo(methodOn(MemberController.class).signin(signupDto)).withSelfRel());
        entityModel.add(new Link("/swagger-ui.html#/auth%20API/loginUsingPOST").withRel("profile"));

        return ResponseEntity.ok().body(entityModel);
    }
}
