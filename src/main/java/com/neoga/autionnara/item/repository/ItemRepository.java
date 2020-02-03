package com.neoga.autionnara.item.repository;

import com.neoga.autionnara.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
