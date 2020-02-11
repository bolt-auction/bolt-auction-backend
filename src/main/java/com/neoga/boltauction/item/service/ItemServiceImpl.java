package com.neoga.boltauction.item.service;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.repository.CategoryRepository;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.dto.UpdateItemDto;
import com.neoga.boltauction.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

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
        } else if (categoryId >= 1 && categoryId <= 6) {
            Category findCategory = categoryRepository.findById(categoryId).get();
            return itemRepository.findAllByCategoryEquals(pageable, findCategory);
        } else {
            return null;
        }
    }

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item, UpdateItemDto updateItemDto) {
        if (updateItemDto.getItemName() != null)
            item.setName(updateItemDto.getItemName());
        else if (updateItemDto.getDescription() != null)
            item.setDescription(updateItemDto.getDescription());
        else if (updateItemDto.getQuickPrice() != 0)
            item.setQuickPrice(updateItemDto.getQuickPrice());
        else if (updateItemDto.getStartPrice() != 0)
            item.setStartPrice(updateItemDto.getStartPrice());
        else if (updateItemDto.getMinBidPrice() != 0)
            item.setMinBidPrice(updateItemDto.getMinBidPrice());
        else if (updateItemDto.getEndDt() != null)
            item.setEndDt(updateItemDto.getEndDt());
        else if (updateItemDto.getCategoryId() != null)
            item.setCategory(categoryRepository.findById(updateItemDto.getCategoryId()).get());

        return itemRepository.save(item);
    }
}