package com.neoga.boltauction.item.controller;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.service.CategoryService;
import com.neoga.boltauction.image.service.ImageService;
import com.neoga.boltauction.image.service.ImageServiceImpl;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.dto.ItemDto;
import com.neoga.boltauction.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/item")
public class ItemController {

    private final ItemService itemService;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final ImageService imageService;

    @GetMapping("category/{category-id}")
    public ResponseEntity getItems(@PathVariable(name = "category-id") Long categoryId, Pageable pageable,
                                   PagedResourcesAssembler<ItemDto> assembler) {
        Page<Item> itemPage = itemService.getItems(categoryId, pageable);

        if (itemPage == null) {
            return ResponseEntity.noContent().build();
        }

        Page<ItemDto> itemDtoPage = itemPage.map(item -> {
                    ItemDto itemDto = modelMapper.map(item, ItemDto.class);
                    itemDto.setItemName(item.getName());
                    itemDto.setCategoryId(item.getCategory().getId());
                    return itemDto;
                }
        );

        PagedModel<EntityModel<ItemDto>> entityModels = assembler.toModel(itemDtoPage, i -> new EntityModel(i));
        entityModels.forEach(entityModel -> entityModel.add(linkTo(methodOn(ItemController.class).getItem(entityModel.getContent().getItemId())).withRel("item-detail")));
        entityModels.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(entityModels);
    }

    @PostMapping
    public ResponseEntity insertItem(ItemDto itemDto,
                                     MultipartFile... images) throws IOException {
        Item item = new Item();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        item = modelMapper.map(itemDto, Item.class);
        item.setName(itemDto.getItemName());
        item.setCreateDt(LocalDateTime.now());

        Optional<Category> optionalCategory = categoryService.getCategory(itemDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return ResponseEntity.noContent().build();
        }

        item.setCategory(optionalCategory.get());

        Item saveItem = itemService.insertItem(item);

        imageService.saveItemImages(saveItem.getId(), images);


        ItemDto saveItemDto = modelMapper.map(saveItem, ItemDto.class);
        saveItemDto.setItemId(saveItem.getId());
        saveItemDto.setItemName(saveItem.getName());
        saveItemDto.setCategoryId(saveItem.getCategory().getId());

        WebMvcLinkBuilder selfLinkBuilder =linkTo(ItemController.class).slash(itemDto.getItemId());
        URI createdUri = selfLinkBuilder.toUri();
        EntityModel entityModel = new EntityModel(saveItemDto);

        entityModel.add(linkTo(methodOn(ItemController.class).getItem(saveItemDto.getItemId())).withRel("item-detail"));
        entityModel.add(linkTo(ItemController.class).withRel("query-events"));
        entityModel.add(selfLinkBuilder.withRel("update-event"));
        entityModel.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(entityModel);
    }

    @GetMapping("/{item-id}")
    public ResponseEntity getItem(@PathVariable(name = "item-id") Long id) {

        Item findItem = itemService.getItem(id);

        if (findItem == null) {
            return ResponseEntity.noContent().build();
        }


        ItemDto itemDto = modelMapper.map(findItem, ItemDto.class);
        EntityModel entityModel = new EntityModel(itemDto);
        entityModel.add(linkTo(ItemController.class).slash(findItem.getId()).withSelfRel());
        entityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(entityModel);

    }

    @DeleteMapping("/{item-id}")
    public ResponseEntity deleteItem(@PathVariable(name = "item-id") Long id) {

       Item deleteItem = itemService.deleteItem(id);

       if (deleteItem == null) {
           return ResponseEntity.noContent().build();
       }

       return ResponseEntity.ok().build();
    }

    @PutMapping("/{item-id}")
    public ResponseEntity updateItem(@PathVariable(name = "item-id") Long id,
                                     ItemDto itemDto/*,
                                     @RequestPart MultipartFile img*/) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Item item = modelMapper.map(itemDto, Item.class);
        Item findItem = itemService.updateItem(id, item);

        if (findItem == null) {
            return ResponseEntity.noContent().build();
        }

        EntityModel entityModel = new EntityModel(itemDto);
        entityModel.add(linkTo(ItemController.class).slash(findItem.getId()).withSelfRel());
        entityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(entityModel);
    }
}
