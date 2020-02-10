package com.neoga.boltauction.bid.service;

import com.neoga.boltauction.bid.domain.Bid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BidService {
    public ResponseEntity getBidList(Long id);

}
