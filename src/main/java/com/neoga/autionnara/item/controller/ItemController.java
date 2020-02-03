package com.neoga.autionnara.item.controller;

import com.neoga.autionnara.item.domain.Item;
import com.neoga.autionnara.item.domain.ItemEntityModel;
import com.neoga.autionnara.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity getItems(Pageable pageable,
                                      PagedResourcesAssembler<Item> assembler) {
        Page<Item> findItems = itemService.findAll(pageable);
        PagedModel<EntityModel<Item>> entityModels = assembler.toModel(findItems);
        entityModels.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(entityModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity getItem(@PathVariable Long id) {
        Optional<Item> optionalItem = itemService.getItem(id);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Item item = optionalItem.get();
        ItemEntityModel itemEntityModel = new ItemEntityModel(item);
        itemEntityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(itemEntityModel);
    }
}
