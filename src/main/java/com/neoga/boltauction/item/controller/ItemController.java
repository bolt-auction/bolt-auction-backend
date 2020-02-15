package com.neoga.boltauction.item.controller;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.service.CategoryService;
import com.neoga.boltauction.exception.custom.CItemNotFoundException;
import com.neoga.boltauction.exception.custom.CNotImageException;
import com.neoga.boltauction.image.service.ImageService;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.dto.InsertItemDto;
import com.neoga.boltauction.item.dto.ItemDto;
import com.neoga.boltauction.item.dto.UpdateItemDto;
import com.neoga.boltauction.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

        Page<ItemDto> itemDtoPage = itemService.getItems(categoryId, pageable);

        PagedResources<Resource<ItemDto>> entityModels = assembler.toResource(itemDtoPage, i -> new Resource<>(i));
        entityModels.forEach(entityModel -> entityModel.add(linkTo(methodOn(ItemController.class).getItem(entityModel.getContent().getItemId())).withRel("item-detail")));
        entityModels.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(entityModels);
    }

    @PostMapping
    public ResponseEntity insertItem(@Valid InsertItemDto insertItemDto,
                                             MultipartFile... images) throws IOException {

        // save item
        ItemDto saveItemDto = itemService.saveItem(insertItemDto, images);

        ControllerLinkBuilder selfLinkBuilder =linkTo(ItemController.class).slash(saveItemDto.getItemId());
        URI createdUri = selfLinkBuilder.toUri();
        Resource entityModel = new Resource(saveItemDto);

        entityModel.add(linkTo(methodOn(ItemController.class).getItem(saveItemDto.getItemId())).withRel("item-detail"));
        entityModel.add(linkTo(ItemController.class).withRel("query-events"));
        entityModel.add(selfLinkBuilder.withRel("update-event"));
        entityModel.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(entityModel);
    }

    @GetMapping("/{item-id}")
    public ResponseEntity getItem(@PathVariable(name = "item-id") Long id) {


        ItemDto findItem = itemService.getItem(id);

        Resource entityModel = new Resource(findItem);
        entityModel.add(linkTo(ItemController.class).slash(findItem.getItemId()).withSelfRel());
        entityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{item-id}")
    public ResponseEntity deleteItem(@PathVariable(name = "item-id") Long id) {

        // delete item entity
        try {
            itemService.deleteItem(id);
        } catch (CItemNotFoundException e) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{item-id}")
    public ResponseEntity updateItem(@PathVariable(name = "item-id") Long id,
                                     @Valid UpdateItemDto updateItemDto,
                                     MultipartFile... images) throws IOException {

        ItemDto itemDto = itemService.updateItem(id, updateItemDto, images);

        Resource entityModel = new Resource(itemDto);
        entityModel.add(linkTo(ItemController.class).slash(itemDto.getItemId()).withSelfRel());
        entityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(entityModel);
    }
}
