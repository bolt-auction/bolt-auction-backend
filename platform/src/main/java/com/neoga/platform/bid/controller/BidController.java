package com.neoga.platform.bid.controller;

import com.neoga.platform.bid.dto.BidDto;
import com.neoga.platform.bid.dto.BidList;
import com.neoga.platform.bid.service.BidService;
import com.neoga.platform.exception.custom.CBidException;
import com.neoga.platform.exception.custom.CItemEndException;
import com.neoga.platform.item.dto.ItemDto;
import com.neoga.platform.item.service.ItemService;
import com.neoga.platform.security.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
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
    public BidList getBidList(@PathVariable(name = "item-id") Long itemId) {
        List<BidDto> bidDtoList = bidService.getBidList(itemId);
        BidList bidList = new BidList();
        bidList.setBidList(bidDtoList);

        return bidList;
    }

    @ApiOperation(value = "입찰 등록", notes = "해당 상품에 입찰 등록")
    @PostMapping("/{item-id}")
    public ResponseEntity registerBidItem(@PathVariable(name = "item-id") Long itemId, int price) {

        ItemDto findItem = itemService.getItem(itemId);

        // 본인의 상품인지 체크
        Long memberId = authService.getLoginInfo().getMemberId();
        Long sellerId = findItem.getSeller().getId();
        if (sellerId.equals(memberId))
            throw new CBidException("본인의 상품입니다.");

        // 상품 시간 지났는지 체크
        if (findItem.getEndDt().isBefore(LocalDateTime.now()))
            throw new CItemEndException("상품이 종료되었습니다.");

        // 가격 비교
        if (findItem.getStartPrice() > price || (findItem.getCurrentPrice() + findItem.getMinBidPrice()) > price)
            throw new CBidException("입찰 가격이 낮습니다.");

        BidDto bid = bidService.saveBid(itemId, price, memberId);

        Resource resource = new Resource(bid);
        resource.add(linkTo(BidController.class).slash(bid.getBidId()).withSelfRel());
        resource.add(new Link("/swagger-ui.html#/bid-controller/registerBidItemUsingGET").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @ApiOperation(value = "입찰 삭제", notes = "")
    @DeleteMapping("/{item-id}")
    public ResponseEntity deleteBidItem(@PathVariable(name = "item-id") Long itemId, @RequestBody Long bidId) {
        Long memberId = authService.getLoginInfo().getMemberId();
        Long findMemberId = bidService.getMemberByBidId(bidId);

        if (memberId.equals(findMemberId)) {
            bidService.deleteBid(bidId);
        }

        return null;
    }
}
