package com.neoga.platform.item.service;

import com.neoga.platform.item.dto.InsertItemDto;
import com.neoga.platform.item.dto.Item;
import com.neoga.platform.item.dto.UpdateItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    Item getItem(Long id);

    Item deleteItem(Long id);

    Page<Item> getItems(Long categoryId, Pageable pageable);

    Item saveItem(InsertItemDto itemDto, Long memberId, MultipartFile... images) throws IOException;

    Item updateItem(Long id, UpdateItemDto updateItemDto, Long memberId, MultipartFile... images) throws IOException;

    List<Item> getItemsByMemberId(Long memberId);

    Page<Item> searchItem(String filter, String search, Pageable pageable);
}
