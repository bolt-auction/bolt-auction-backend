package com.neoga.boltauction.item.service;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.repository.CategoryRepository;
import com.neoga.boltauction.exception.custom.CCategoryNotFoundException;
import com.neoga.boltauction.exception.custom.CItemNotFoundException;
import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.image.service.ImageService;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.item.dto.InsertItemDto;
import com.neoga.boltauction.item.dto.ItemDto;
import com.neoga.boltauction.item.dto.UpdateItemDto;
import com.neoga.boltauction.item.repository.ItemRepository;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import com.neoga.boltauction.memberstore.store.domain.Store;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final JSONParser parser = new JSONParser();

    @Override
    public ItemDto getItem(Long id) {

        Item findItem;

        // get item entity
        findItem = itemRepository.findById(id).orElseThrow(CItemNotFoundException::new);

        // map findItem -> itemDto
        ItemDto itemDto = modelMapper.map(findItem, ItemDto.class);

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

        // get item entity
        if (categoryId == NO_CATEGORY) {
            itemPage = itemRepository.findAll(pageable);
        } else if (categoryId >= FIRST_CATEGORY && categoryId <= LAST_CATEGORY) {
            Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new CCategoryNotFoundException("카테고리를 찾을 수 없습니다."));
            itemPage = itemRepository.findAllByCategoryEquals(pageable, findCategory);
        } else {
            throw new CCategoryNotFoundException("존제하지 않는 카테고리 입니다");
        }

        // map item -> itemDto
        return itemPage.map(item -> mapItemItemDto(item));
    }

    @Override
    public ItemDto saveItem(InsertItemDto insertItemDto, Long memberId, MultipartFile... images) throws IOException {

        // find store
        Store findStore = memberRepository.getOne(memberId).getStore();

        // map insertItemDto -> item
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Item item = modelMapper.map(insertItemDto, Item.class);
        item.setCurrentPrice(insertItemDto.getStartPrice());
        item.setStore(findStore);
        Category findCategory = categoryRepository.findById(insertItemDto.getCategoryId()).orElseThrow(() -> new CCategoryNotFoundException("카테고리를 찾을 수 없습니다."));
        item.setCategory(findCategory);

        Item saveItem = itemRepository.save(item);

        imageService.saveItemImages(saveItem, images);

        return mapItemItemDto(saveItem);
    }

    @Override
    public ItemDto updateItem(Long id, UpdateItemDto updateItemDto, Long memberId, MultipartFile... images) throws IOException {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // find Item
        Item findItem = itemRepository.findById(id).orElseThrow(CItemNotFoundException::new);

        modelMapper.map(updateItemDto, findItem);
        Category findCategory = categoryRepository.findById(updateItemDto.getCategoryId()).orElseThrow(() -> new CCategoryNotFoundException("카테고리를 찾을 수 없습니다."));
        findItem.setCategory(findCategory);

        // update image
        imageService.updateItemImages(findItem, images);

        // save item
        itemRepository.save(findItem);

        return mapItemItemDto(findItem);
    }

    @Override
    public Page<ItemDto> searchItem(String filter, String search, Pageable pageable){

        Page<Item> searchItems = null;
        if (filter.equals("name")) {
            searchItems = itemRepository.findAllByNameIsContaining(pageable, search);
        }
        return searchItems.map(item -> mapItemItemDto(item));
    }

    @Override
    public List<ItemDto> getItemsByStore(Long storeId) {
        List<Item> findItems = itemRepository.findAllByStore_Id(storeId);
        return findItems.stream().map(item -> mapItemItemDto(item)).collect(Collectors.toList());
    }

    private ItemDto mapItemItemDto(Item item) {
        ItemDto itemDto = modelMapper.map(item, ItemDto.class);
        itemDto.setStoreId(item.getStore().getId());
        itemDto.setSellerId(item.getStore().getMembers().getId());
        try {
            itemDto.setImagePath((JSONObject) parser.parse(item.getImagePath()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return itemDto;
    }
}
