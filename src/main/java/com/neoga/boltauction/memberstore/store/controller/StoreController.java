package com.neoga.boltauction.memberstore.store.controller;

import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.repository.StoreRepository;
import com.neoga.boltauction.memberstore.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreController {

    private StoreService storeService;
    private final StoreRepository storeRepository;

    @GetMapping("{store-id}")
    public Store getStore(@PathVariable(name = "store-id") Long storeId) {
        return storeRepository.findById(storeId).get().getMembers().getStore();
    }
}
