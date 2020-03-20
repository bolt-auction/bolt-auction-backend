package com.neoga.platform.bid.service;

import com.neoga.platform.bid.dto.BidDto;

import java.util.List;

public interface BidService {
    List<BidDto> getBidList(Long id);

    BidDto saveBid(Long itemId, int price, Long memberId);

    Long getMemberByBidId(Long bidId);

    void deleteBid(Long bidId);
}
