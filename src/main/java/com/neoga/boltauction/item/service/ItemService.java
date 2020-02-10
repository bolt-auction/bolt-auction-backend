package com.neoga.boltauction.item.service;

import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import java.util.List;

public interface ItemService {

    Item getItem(Long id);

    Item deleteItem(Long id);

    Page<Item> getItems(Long categoryId, Pageable pageable);

    Item insertItem(Item item);

    Item updateItem(Long id, Item item);
}
