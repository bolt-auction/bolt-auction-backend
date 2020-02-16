package com.neoga.boltauction.bid.controller;

import com.neoga.boltauction.bid.dto.BidDto;
import com.neoga.boltauction.bid.service.BidService;
import com.neoga.boltauction.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bid")
public class BidController {

    private final BidService bidService;
    private final AuthService authService;

    @GetMapping("/{item-id}")
    public ResponseEntity getBidList(@PathVariable(name = "item-id") Long itemId) {
        List<BidDto> bidList = bidService.getBidList(itemId);
        Resources<BidDto> bidDtoResources = new Resources<>(bidList);



        return ResponseEntity.ok(bidDtoResources);
    }

    @PostMapping("/{item-id}")
    public ResponseEntity registerBidItem(@PathVariable(name = "item-id") Long itemId, int price) {
        Long memberId = authService.getLoginInfo().getMemberId();
        Resource<BidDto> bidDtoResource = bidService.saveBid(itemId, price, memberId);

        return ResponseEntity.ok(bidDtoResource);
    }

    @DeleteMapping("/{item-id")
    public ResponseEntity deleteBidItem() {
        return null;
    }
}
