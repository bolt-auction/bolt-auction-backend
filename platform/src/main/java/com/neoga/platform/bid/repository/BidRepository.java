package com.neoga.platform.bid.repository;

import com.neoga.platform.bid.domain.Bid;
import com.neoga.platform.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByItemOrderByPriceAsc(Item item);
    List<Bid> findAllByItemOrderByPriceDesc(Item item);
}
