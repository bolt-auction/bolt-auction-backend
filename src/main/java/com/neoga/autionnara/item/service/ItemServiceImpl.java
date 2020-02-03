package com.neoga.autionnara.item.service;

import com.neoga.autionnara.item.domain.Item;
import com.neoga.autionnara.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Item> getItemList() {
        return itemRepository.findAll();
    }

    @Override
    public void insertItem(Item item) {

    }

    @Override
    public Optional<Item> getItem(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public void updateItem(Item item) {

    }

    @Override
    public void deleteItem(Item item) {

    }

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }
}
