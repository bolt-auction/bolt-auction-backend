package com.neoga.boltauction.item.service;

import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.domain.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {

    ResponseEntity getItem(Long id);

    ResponseEntity<Object> deleteItem(Long id);

    ResponseEntity getItems(Long categoryId, Pageable pageable, PagedResourcesAssembler<ItemDto> assembler);

    ResponseEntity insertItem(ItemDto itemDto, Errors errors/*, MultipartFile[] images*/);

    ResponseEntity updateItem(Long id, ItemDto itemDto);
}
