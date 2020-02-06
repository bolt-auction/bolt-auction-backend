package com.neoga.boltauction.item.repository;

import com.neoga.boltauction.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
