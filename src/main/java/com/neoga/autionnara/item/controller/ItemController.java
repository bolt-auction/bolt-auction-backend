package com.neoga.autionnara.item.controller;

import com.neoga.autionnara.category.domain.Category;
import com.neoga.autionnara.category.repository.CategoryRepository;
import com.neoga.autionnara.item.domain.Item;
import com.neoga.autionnara.item.domain.ItemDto;
import com.neoga.autionnara.item.domain.ItemEntityModel;
import com.neoga.autionnara.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;


    @GetMapping("category/{category-id}")
    public ResponseEntity getItems(@PathVariable(name = "category-id") Long categoryId, Pageable pageable,
                                   PagedResourcesAssembler<ItemDto> assembler) {
        Page<Item> findItems = itemService.findAll(pageable);
        Page<ItemDto> itemDtoPage = findItems.map(i -> modelMapper.map(i, ItemDto.class));
        PagedModel<EntityModel<ItemDto>> entityModels = assembler.toModel(itemDtoPage, i ->new ItemEntityModel(i));
        entityModels.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(entityModels);
    }

    @PostMapping
    public ResponseEntity insertItem(@RequestBody @Valid ItemDto itemDto,
                                     Errors errors /*, @RequestPart MultipartFile... images*/) {
        Item item = new Item();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        item = modelMapper.map(itemDto, Item.class);
        item.setName(itemDto.getItemName());
        Optional<Category> optionalCategory = categoryRepository.findById(itemDto.getCategoryId());

        item.setCategory(optionalCategory.get());

        itemService.insertItem(item);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(ItemController.class).slash(item.getId());
        URI createdUri = selfLinkBuilder.toUri();
        ItemEntityModel itemEntityModel = new ItemEntityModel(itemDto);
        itemEntityModel.add(linkTo(ItemController.class).withRel("query-events"));
        itemEntityModel.add(selfLinkBuilder.withRel("update-event"));
        itemEntityModel.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(itemEntityModel);
    }

    @GetMapping("/{item-id}")
    public ResponseEntity getItem(@PathVariable(name = "item-id") Long id) {
        Optional<Item> optionalItem = itemService.getItem(id);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Item findItem = optionalItem.get();
        ItemDto itemDto = modelMapper.map(findItem, ItemDto.class);
        ItemEntityModel itemEntityModel = new ItemEntityModel(itemDto);
        itemEntityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(itemEntityModel);
    }

    @DeleteMapping("/{item-id}")
    public ResponseEntity deleteItem(@PathVariable(name = "item-id") Long id) {
        Optional<Item> optionalItem = itemService.getItem(id);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        itemService.deleteItem(optionalItem.get());

        return null;
    }

    @PutMapping("/{item-id}")
    public ResponseEntity updateItem(@PathVariable(name = "item-id") Long id,
                                     @RequestBody @Valid ItemDto itemDto/*,
                                     @RequestPart MultipartFile img*/) {
        Optional<Item> optionalItem = itemService.getItem(id);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Item findItem = optionalItem.get();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(itemDto, findItem);
        itemService.updateItem(findItem);
        modelMapper.map(findItem, itemDto);
        ItemEntityModel itemEntityModel = new ItemEntityModel(itemDto);
        itemEntityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(itemEntityModel);
    }
}
