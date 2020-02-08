package com.neoga.boltauction.item.service;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.repository.CategoryRepository;
import com.neoga.boltauction.item.controller.ItemController;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.domain.ItemDto;
import com.neoga.boltauction.item.domain.ItemEntityModel;
import com.neoga.boltauction.item.repository.ItemRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseEntity getItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Item findItem = optionalItem.get();
        ItemDto itemDto = modelMapper.map(findItem, ItemDto.class);
        ItemEntityModel itemEntityModel = new ItemEntityModel(itemDto);
        itemEntityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(itemEntityModel);
    }

    @Override
    public ResponseEntity updateItem(Long id, ItemDto itemDto) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Item findItem = optionalItem.get();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(itemDto, findItem);
        itemRepository.save(findItem);
        modelMapper.map(findItem, itemDto);
        ItemEntityModel itemEntityModel = new ItemEntityModel(itemDto);
        itemEntityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(itemEntityModel);
    }

    @Override
    public ResponseEntity<Object> deleteItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (!optionalItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        itemRepository.delete(optionalItem.get());

        return null;
    }

    @Override
    public ResponseEntity getItems(Long categoryId, Pageable pageable, PagedResourcesAssembler<ItemDto> assembler) {
        Page<Item> findItems = itemRepository.findAllByOrderByCreateDtAsc(pageable);

        Page<ItemDto> itemDtoPage = findItems.map(item -> {
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

    @Override
    public ResponseEntity insertItem(ItemDto itemDto, Errors errors/*, MultipartFile[] images*/) {
        Item item = new Item();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        item = modelMapper.map(itemDto, Item.class);
        item.setName(itemDto.getItemName());
        item.setCreateDt(LocalDateTime.now());
        Optional<Category> optionalCategory = categoryRepository.findById(itemDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return ResponseEntity.notFound().build();
        }


        item.setCategory(optionalCategory.get());

        Item saveItem = itemRepository.save(item);

        ItemDto mappedItemDto = modelMapper.map(saveItem, ItemDto.class);
        mappedItemDto.setItemId(saveItem.getId());
        mappedItemDto.setItemName(saveItem.getName());
        mappedItemDto.setCategoryId(saveItem.getCategory().getId());

        WebMvcLinkBuilder selfLinkBuilder =linkTo(ItemController.class).slash(itemDto.getItemId());
        URI createdUri = selfLinkBuilder.toUri();
        ItemEntityModel itemEntityModel = new ItemEntityModel(mappedItemDto);
        itemEntityModel.add(linkTo(ItemController.class).withRel("query-events"));
        itemEntityModel.add(selfLinkBuilder.withRel("update-event"));
        itemEntityModel.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(itemEntityModel);
    }
}
