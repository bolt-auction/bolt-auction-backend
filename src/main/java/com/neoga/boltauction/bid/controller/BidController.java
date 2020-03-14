package com.neoga.boltauction.bid.controller;

import com.neoga.boltauction.bid.dto.BidDto;
import com.neoga.boltauction.bid.service.BidService;
import com.neoga.boltauction.exception.custom.CItemEndException;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.service.ItemService;
import com.neoga.boltauction.security.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bid")
public class BidController {

    private final BidService bidService;
    private final AuthService authService;
    private final ItemService itemService;

    @ApiOperation(value = "입찰 리스트 조회", notes = "해당 상품의 모든 입찰 조회")
    @GetMapping("/{item-id}")
    public ResponseEntity getBidList(@PathVariable(name = "item-id") Long itemId) {
        List<BidDto> bidList = bidService.getBidList(itemId);
        Resources<BidDto> bidDtoResources = new Resources<>(bidList);

        return ResponseEntity.ok(bidDtoResources);
    }

    @ApiOperation(value = "입찰 등록", notes = "해당 상품에 입찰 등록")
    @PostMapping("/{item-id}")
    public ResponseEntity registerBidItem(@PathVariable(name = "item-id") Long itemId, int price) {

        Item findItem = itemService.getItem(itemId);

        // 본인의 상품인지 체크
        Long memberId = authService.getLoginInfo().getMemberId();
        Long sellerId = findItem.getMembers().getId();
        if (sellerId == memberId)
            throw new RuntimeException("본인의 상품입니다.");

        // 상품 시간 지났는지 체크
        if (findItem.getEndDt().isBefore(LocalDateTime.now()))
            throw new CItemEndException("상품이 종료되었습니다.");

        // 가격 비교
        if (findItem.getStartPrice() > price || findItem.getCurrentPrice() >= price )
            throw new RuntimeException("입찰 가격이 낮습니다.");

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
        Long findMemberId = bidService.getMemberByBidId(bidId);

        if (memberId == findMemberId){
            bidService.deleteBid(bidId);
        }

        return null;
    }
}
