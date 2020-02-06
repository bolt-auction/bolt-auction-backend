package com.neoga.boltauction.item.controller;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.repository.CategoryRepository;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.domain.ItemDto;
import com.neoga.boltauction.item.domain.ItemEntityModel;
import com.neoga.boltauction.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("category/{category-id}")
    public ResponseEntity getItems(@PathVariable(name = "category-id") Long categoryId, Pageable pageable,
                                   PagedResourcesAssembler<ItemDto> assembler) {
        return itemService.getItems(categoryId, pageable, assembler);
    }

    @PostMapping
    public ResponseEntity insertItem(@RequestBody @Valid ItemDto itemDto,
                                     Errors errors/*, @RequestPart MultipartFile... images*/) {
        return itemService.insertItem(itemDto, errors/*, images*/);
    }

    @GetMapping("/{item-id}")
    public ResponseEntity getItem(@PathVariable(name = "item-id") Long id) {
        return itemService.getItem(id);

    }

    @DeleteMapping("/{item-id}")
    public ResponseEntity deleteItem(@PathVariable(name = "item-id") Long id) {
        return itemService.deleteItem(id);
    }

    @PutMapping("/{item-id}")
    public ResponseEntity updateItem(@PathVariable(name = "item-id") Long id,
                                     @RequestBody @Valid ItemDto itemDto/*,
                                     @RequestPart MultipartFile img*/) {
        return itemService.updateItem(id, itemDto/*, img*/);

    }
}
