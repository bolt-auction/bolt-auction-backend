package com.neoga.platform.item.service;

import com.neoga.platform.category.domain.Category;
import com.neoga.platform.category.repository.CategoryRepository;
import com.neoga.platform.exception.custom.CCategoryNotFoundException;
import com.neoga.platform.exception.custom.CItemNotFoundException;
import com.neoga.platform.image.service.ImageService;
import com.neoga.platform.item.dto.InsertItemDto;
import com.neoga.platform.item.dto.Item;
import com.neoga.platform.item.dto.UpdateItemDto;
import com.neoga.platform.item.repository.ItemRepository;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    private static final String NO_ITEM = "상품이 존재하지 않습니다.";

    @Override
    public Item getItem(Long id) {

        com.neoga.platform.item.domain.Item findItem;

        // get item entity
        findItem = itemRepository.findById(id).orElseThrow(() -> new CItemNotFoundException(NO_ITEM));

        return mapItemItemDto(findItem);
    }

    @Override
    public Item deleteItem(Long id) {
        com.neoga.platform.item.domain.Item item = itemRepository.findById(id).orElseThrow(() -> new CItemNotFoundException(NO_ITEM));
        itemRepository.delete(item);

        return mapItemItemDto(item);
    }

    @Override
    public Page<Item> getItems(Long categoryId, Pageable pageable) {

        Page<com.neoga.platform.item.domain.Item> itemPage;

        // get item entity
        if (categoryId == NO_CATEGORY) {
            itemPage = itemRepository.findAll(pageable);
        } else if (categoryId >= FIRST_CATEGORY && categoryId <= LAST_CATEGORY) {
            itemPage = itemRepository.findAllByCategory_IdEquals(pageable, categoryId);
        } else {
            throw new CCategoryNotFoundException("존제하지 않는 카테고리 입니다");
        }

        // map item -> itemDto
        return itemPage.map(this::mapItemItemDto);
    }

    @Override
    public Item saveItem(InsertItemDto insertItemDto, Long memberId, MultipartFile... images) throws IOException {

        // find store
        Members findMembers = memberRepository.getOne(memberId);

        // map insertItemDto -> item
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        com.neoga.platform.item.domain.Item item = modelMapper.map(insertItemDto, com.neoga.platform.item.domain.Item.class);
        item.setCurrentPrice(insertItemDto.getStartPrice());
        item.setMembers(findMembers);
        Category findCategory = categoryRepository.findById(insertItemDto.getCategoryId()).orElseThrow(() -> new CCategoryNotFoundException("카테고리를 찾을 수 없습니다."));
        item.setCategory(findCategory);
        item.setBidCount(0);

        com.neoga.platform.item.domain.Item saveItem = itemRepository.save(item);

        imageService.saveItemImages(saveItem, images);

        return mapItemItemDto(saveItem);
    }

    @Override
    public Item updateItem(Long id, UpdateItemDto updateItemDto, Long memberId, MultipartFile... images) throws IOException {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // find Item
        com.neoga.platform.item.domain.Item findItem = itemRepository.findById(id).orElseThrow(() -> new CItemNotFoundException(NO_ITEM));

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
    public Page<Item> searchItem(String filter, String search, Pageable pageable) {

        Page<com.neoga.platform.item.domain.Item> searchItems = null;
        if (filter.equals("name")) {
            searchItems = itemRepository.findAllByNameIsContaining(pageable, search);
        }
        return searchItems.map(this::mapItemItemDto);
    }

    @Override
    public List getItemsByMemberId(Long memberId) {
        return itemRepository.findAllByMembers_Id(memberId);
    }

    private Item mapItemItemDto(com.neoga.platform.item.domain.Item item) {
        Item itemDto = modelMapper.map(item, Item.class);

        itemDto.setSeller(item.getMembers());

        if (item.getImagePath() == null) {
            itemDto.setImagePath(null);
        } else {
            String[] pathArray = item.getImagePath().split(",");
            itemDto.setImagePath(pathArray);
        }

        return itemDto;
    }
}
