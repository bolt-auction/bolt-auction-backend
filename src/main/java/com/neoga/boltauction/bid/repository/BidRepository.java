package com.neoga.boltauction.bid.repository;

import com.neoga.boltauction.bid.domain.Bid;
import com.neoga.boltauction.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByItemOrderByPriceDesc(Item item);
}
