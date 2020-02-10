package com.neoga.boltauction.item.service;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.repository.CategoryRepository;
import com.neoga.boltauction.item.controller.ItemController;
import com.neoga.boltauction.item.domain.Item;
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
    public Item getItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (!optionalItem.isPresent()) {
            return null;
        }

        Item findItem = optionalItem.get();

        return findItem;
    }

    @Override
    public Item updateItem(Long id, Item item) {

        Optional<Item> optionalItem = itemRepository.findById(id);
        if (!optionalItem.isPresent()) {
            return null;
        }

        Item findItem = optionalItem.get();
        itemRepository.save(findItem);

        return findItem;
    }

    @Override
    public Item deleteItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (!optionalItem.isPresent()) {
            return null;
        }

        Item item = optionalItem.get();

        itemRepository.delete(optionalItem.get());

        return item;
    }

    @Override
    public Page<Item> getItems(Long categoryId, Pageable pageable) {

        if (categoryId == 0) {
            return itemRepository.findAllByOrderByCreateDtAsc(pageable);
        } else if (categoryId >= 1 && categoryId <=6) {
            Category findCategory = categoryRepository.findById(categoryId).get();
            return itemRepository.findAllByCategoryEquals(pageable, findCategory);
        } else {
            return null;
        }
    }

    @Override
    public Item insertItem(Item item) {
        return itemRepository.save(item);
    }
}
