package com.neoga.autionnara.item.service;

import com.neoga.autionnara.item.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getItemList();

    void insertItem(Item item);

    Optional<Item> getItem(Long id);

    void updateItem(Item item);

    void deleteItem(Item item);

    Page<Item> findAll(Pageable pageable);
}
