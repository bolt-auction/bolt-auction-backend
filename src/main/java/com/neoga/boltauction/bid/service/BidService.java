package com.neoga.boltauction.bid.service;

import com.neoga.boltauction.bid.dto.BidDto;
import org.springframework.hateoas.Resource;

import java.util.List;

public interface BidService {
    List<BidDto> getBidList(Long id);

    BidDto saveBid(Long itemId, int price, Long memberId);

    Long getMemberByBidId(Long bidId);

    void deleteBid(Long bidId);
}
