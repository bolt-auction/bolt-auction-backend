package com.neoga.boltauction.memberstore.store.controller;

import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.service.MemberService;
import com.neoga.boltauction.memberstore.store.dto.StoreDto;
import com.neoga.boltauction.memberstore.store.service.StoreService;
import com.neoga.boltauction.security.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;
    private final AuthService authService;
    private final MemberService memberService;

    @ApiOperation(value = "상점 조회", notes = "해당 상점의 정보 조회")
    @GetMapping("{member-id}")
    public ResponseEntity getStore(@PathVariable(name = "member-id") Long memberId) {

        StoreDto findStoreDto = storeService.getStore(memberId);

        Resource storeResource = new Resource(findStoreDto);
        storeResource.add(linkTo(StoreController.class).slash(findStoreDto.getMemberId()).withSelfRel());
        storeResource.add(new Link("/swagger-ui.html#/store-controller/getStoreUsingGET").withRel("profile"));

        return ResponseEntity.ok(storeResource);
    }

    @ApiOperation(value = "상점 수정", notes = "상점 설명 이미지 등록")
    @PutMapping("{member-id}")
    public ResponseEntity updateStore(@PathVariable(name = "member-id") Long memberId,
                                      String description, String memberName, MultipartFile image) throws IOException {
        // 해당 유저인지 체크
        Long currentMemberId = authService.getLoginInfo().getMemberId();
        Members findMember = memberService.findMemberById(currentMemberId);
        if (findMember.getId() != memberId) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        StoreDto storeDto = storeService.updateStore(findMember, description, memberName, image);

        Resource storeResource = new Resource(storeDto);
        storeResource.add(linkTo(StoreController.class).slash(findMember.getId()).withSelfRel());
        storeResource.add(new Link("/swagger-ui.html#/store-controller/updateStoreUsingPUT").withRel("profile"));

        return ResponseEntity.ok(storeResource);
    }

    // 이미지 null 값
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(MultipartFile.class, "image",new StringTrimmerEditor(true));
    }
}
