package com.neoga.boltauction.bid.repository;

import com.neoga.boltauction.bid.domain.Bid;
import com.neoga.boltauction.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByItemOrderByPriceAsc(Item item);
    Optional<Bid> findBidByItem_IdAndMembers_Id(Long itemId, Long memberId);
}
