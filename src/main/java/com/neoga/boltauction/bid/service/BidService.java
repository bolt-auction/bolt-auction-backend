package com.neoga.boltauction.bid.service;

import com.neoga.boltauction.bid.dto.BidDto;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;

public interface BidService {
    ResponseEntity getBidList(Long id);

    Resource<BidDto> saveBid(Long itemId, int price, Long memberId);
}
