package com.neoga.boltauction.item.service;

import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.dto.InsertItemDto;
import com.neoga.boltauction.item.dto.ItemDto;
import com.neoga.boltauction.item.dto.UpdateItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    ItemDto getItem(Long id);

    Item deleteItem(Long id);

    Page<ItemDto> getItems(Long categoryId, Pageable pageable);

    ItemDto saveItem(InsertItemDto itemDto, Long memberId, MultipartFile... images) throws IOException;

    ItemDto updateItem(Long id, UpdateItemDto updateItemDto, Long memberId, MultipartFile... images) throws IOException;

    List<ItemDto> getItemsByMemberId(Long memberId);

    Page<ItemDto> searchItem(String filter, String search, Pageable pageable);
}
