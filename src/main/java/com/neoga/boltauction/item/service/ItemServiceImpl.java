package com.neoga.boltauction.item.service;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.repository.CategoryRepository;
import com.neoga.boltauction.category.service.CategoryService;
import com.neoga.boltauction.exception.custom.CCategoryNotFoundException;
import com.neoga.boltauction.exception.custom.CItemNotFoundException;
import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.exception.custom.CNotImageException;
import com.neoga.boltauction.image.service.ImageService;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.dto.InsertItemDto;
import com.neoga.boltauction.item.dto.ItemDto;
import com.neoga.boltauction.item.dto.UpdateItemDto;
import com.neoga.boltauction.item.repository.ItemRepository;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final MemberRepository memberRepository;

    private static final int NO_CATEGORY = 0;
    private static final int FIRST_CATEGORY = 1;
    private static final int LAST_CATEGORY = 53;

    @Override
    public ItemDto getItem(Long id) {

        Item findItem = itemRepository.findById(id).orElseThrow(CItemNotFoundException::new);

        // map findItem -> itemDto
        ItemDto itemDto = modelMapper.map(findItem, ItemDto.class);
        itemDto.setItemId(findItem.getId());
        itemDto.setItemName(findItem.getName());
        itemDto.setCategoryId(findItem.getCategory().getId());
        itemDto.setStoreId(findItem.getStore().getId());

        return itemDto;
    }

    @Override
    public Item deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(CItemNotFoundException::new);
        itemRepository.delete(item);

        return item;
    }

    @Override
    public Page<ItemDto> getItems(Long categoryId, Pageable pageable) {

        Page<Item> itemPage;
        JSONParser parser = new JSONParser();

        // get item entity
        if (categoryId == NO_CATEGORY) {
            itemPage = itemRepository.findAll(pageable);
        } else if (categoryId >= FIRST_CATEGORY && categoryId <= LAST_CATEGORY) {
            Category findCategory = categoryRepository.findById(categoryId).orElseThrow(CCategoryNotFoundException::new);
            itemPage = itemRepository.findAllByCategoryEquals(pageable, findCategory);
        } else {
            throw new CCategoryNotFoundException();
        }

        // map item -> itemDto
        Page<ItemDto> itemDtoPage = itemPage.map(item -> {
            ItemDto itemDto = modelMapper.map(item, ItemDto.class);
            itemDto.setItemId(item.getId());
            itemDto.setItemName(item.getName());
            itemDto.setCategoryId(item.getCategory().getId());
            itemDto.setImagePath(null);
            itemDto.setStoreId(item.getStore().getId());
            return itemDto;
        });

        return itemDtoPage;
    }

    @Override
    public ItemDto saveItem(InsertItemDto insertItemDto, Long memberId, MultipartFile... images) throws IOException {

        Members members = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
        Long storeId = members.getStore().getId();

        // map insertItemDto -> item
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Item item = modelMapper.map(insertItemDto, Item.class);
        item.setName(insertItemDto.getItemName());
        item.setCreateDt(LocalDateTime.now());
        Category findCategory = categoryRepository.findById(insertItemDto.getCategoryId()).orElseThrow(CCategoryNotFoundException::new);
        item.setCategory(findCategory);

        Item saveItem = itemRepository.save(item);
        String pathList = imageService.saveItemImages(saveItem.getId(), images);

        ItemDto saveItemDto = modelMapper.map(saveItem, ItemDto.class);
        saveItemDto.setItemId(saveItem.getId());
        saveItemDto.setItemName(saveItem.getName());
        saveItemDto.setCategoryId(saveItem.getCategory().getId());
        try {
            JSONParser parser = new JSONParser();
            saveItemDto.setImagePath((JSONObject) parser.parse(pathList));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return saveItemDto;
    }

    @Override
    public ItemDto updateItem(Long id, UpdateItemDto updateItemDto, MultipartFile[] images) throws IOException {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // find Item
        Item findItem = itemRepository.findById(id).orElseThrow(CItemNotFoundException::new);

        modelMapper.map(updateItemDto, findItem);
        findItem.setName(updateItemDto.getItemName());
        Category findCategory = categoryRepository.findById(updateItemDto.getCategoryId()).orElseThrow(CCategoryNotFoundException::new);
        findItem.setCategory(findCategory);

        // update image
        String path = imageService.updateItemImages(id, images);
        findItem.setImagePath(path);


        // save item
        itemRepository.save(findItem);

        ItemDto itemDto = modelMapper.map(findItem, ItemDto.class);
        itemDto.setItemId(findItem.getId());
        itemDto.setItemName(findItem.getName());
        try {
            JSONParser parser = new JSONParser();
            itemDto.setImagePath((JSONObject) parser.parse(path));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return itemDto;
    }
}
