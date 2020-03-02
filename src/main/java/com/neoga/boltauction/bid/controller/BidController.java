package com.neoga.boltauction.bid.controller;

import com.neoga.boltauction.bid.dto.BidDto;
import com.neoga.boltauction.bid.service.BidService;
import com.neoga.boltauction.item.controller.ItemController;
import com.neoga.boltauction.security.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bid")
public class BidController {

    private final BidService bidService;
    private final AuthService authService;

    @ApiOperation(value = "입찰 리스트 조회", notes = "해당 상품의 모든 입찰 조회")
    @GetMapping("/{item-id}")
    public ResponseEntity getBidList(@PathVariable(name = "item-id") Long itemId) {
        List<BidDto> bidList = bidService.getBidList(itemId);
        Resources<BidDto> bidDtoResources = new Resources<>(bidList);

        return ResponseEntity.ok(bidDtoResources);
    }

    @ApiOperation(value = "입찰 등록", notes = "해당 상품에 입찰 등록")
    @PostMapping("/{item-id}")
    public ResponseEntity registerBidItem(@PathVariable(name = "item-id") Long itemId, @RequestBody int price) {
        Long memberId = authService.getLoginInfo().getMemberId();
        BidDto bidDto = bidService.saveBid(itemId, price, memberId);

        Resource resource = new Resource(bidDto);
        resource.add(linkTo(BidController.class).slash(bidDto.getBidId()).withSelfRel());
        resource.add(new Link("/swagger-ui.html#/bid-controller/registerBidItemUsingGET").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @ApiOperation(value = "입찰 삭제", notes = "")
    @DeleteMapping("/{item-id}")
    public ResponseEntity deleteBidItem(@PathVariable(name = "item-id") Long itemId, @RequestBody Long bidId) {
        Long memberId = authService.getLoginInfo().getMemberId();
        Long findMemberId = bidService.getMemberId(bidId);

        if (memberId == findMemberId){
            bidService.deleteBid(bidId);
        }

        return null;
    }
}
