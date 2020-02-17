package com.neoga.boltauction.memberstore.store.controller;

import com.neoga.boltauction.memberstore.member.service.MemberService;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.service.StoreService;
import com.neoga.boltauction.security.service.AuthService;
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

    @GetMapping("{store-id}")
    public ResponseEntity getStore(@PathVariable(name = "store-id") Long storeId) {

        Store findStore = storeService.getStore(storeId);

        Resource<Store> storeResource = new Resource(findStore);
        storeResource.add(linkTo(StoreController.class).slash(findStore.getId()).withSelfRel());
        storeResource.add(new Link("/swagger-ui.html#/store-controller/getStoreUsingGET").withRel("profile"));

        return ResponseEntity.ok(storeResource);
    }

    @PutMapping("{store-id}")
    public ResponseEntity updateStore(@PathVariable(name = "store-id") Long storeId,
                                      String description, MultipartFile image) throws IOException {
        // 해당 유저인지 체크
        Long memberId = authService.getLoginInfo().getMemberId();
        Store findStore = memberService.findMemberById(memberId).getStore();
        if (findStore.getId() != storeId) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        storeService.updateStore(findStore, description, image);

        Resource<Store> storeResource = new Resource(findStore);
        storeResource.add(linkTo(StoreController.class).slash(findStore.getId()).withSelfRel());
        storeResource.add(new Link("/swagger-ui.html#/store-controller/updateStoreUsingPUT").withRel("profile"));

        return ResponseEntity.ok(storeResource);
    }

    // 이미지 null 값
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(MultipartFile.class, "image",new StringTrimmerEditor(true));
    }
}
