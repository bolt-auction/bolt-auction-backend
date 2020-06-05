package com.neoga.platform.item.service;

import com.neoga.platform.category.domain.Category;
import com.neoga.platform.category.repository.CategoryRepository;
import com.neoga.platform.exception.custom.CCategoryNotFoundException;
import com.neoga.platform.exception.custom.CItemNotFoundException;
import com.neoga.platform.image.service.ImageService;
import com.neoga.platform.item.domain.Item;
import com.neoga.platform.item.dto.InsertItemDto;
import com.neoga.platform.item.dto.ItemDto;
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
    private static final String NO_ITEM = "상품이 존재하지 않습니다.";

    @Override
    public ItemDto getItem(Long id) {
        Item findItem = itemRepository.findById(id).orElseThrow(() -> new CItemNotFoundException(NO_ITEM));

        return mapItemItemDto(findItem);
    }

    @Override
    public ItemDto deleteItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new CItemNotFoundException(NO_ITEM));
        itemRepository.delete(item);

        return mapItemItemDto(item);
    }

    @Override
    public Page<ItemDto> getItems(Long categoryId, Pageable pageable) {

        Page<Item> itemPage;

        // get item entity
        if (categoryId == NO_CATEGORY) {
            itemPage = itemRepository.findAllItems(pageable);
        } else if (categoryId >= FIRST_CATEGORY && categoryId <= LAST_CATEGORY) {
            itemPage = itemRepository.findAllByCategory_IdEquals(pageable, categoryId);
        } else {
            throw new CCategoryNotFoundException("존제하지 않는 카테고리 입니다");
        }

        // map item -> itemDto
        return itemPage.map(this::mapItemItemDto);
    }

    @Override
    public ItemDto saveItem(InsertItemDto insertItemDto, Long memberId, MultipartFile... images) throws IOException {

        // find store
        Members findMembers = memberRepository.getOne(memberId);

        // map insertItemDto -> item
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Item item = modelMapper.map(insertItemDto, Item.class);
        item.setCurrentPrice(insertItemDto.getStartPrice());
        item.setMembers(findMembers);
        Category findCategory = categoryRepository.findById(insertItemDto.getCategoryId()).orElseThrow(() -> new CCategoryNotFoundException("카테고리를 찾을 수 없습니다."));
        item.setCategory(findCategory);
        item.setBidCount(0);

        Item saveItem = itemRepository.save(item);

        imageService.saveItemImages(saveItem, images);

        return mapItemItemDto(saveItem);
    }

    @Override
    public ItemDto updateItem(Long id, UpdateItemDto updateItemDto, Long memberId, MultipartFile... images) throws IOException {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // find Item
        Item findItem = itemRepository.findById(id).orElseThrow(() -> new CItemNotFoundException(NO_ITEM));

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
    public Page<ItemDto> searchItem(String filter, String search, Pageable pageable) {

        Page<Item> searchItems = null;
        if (filter.equals("name")) {
            searchItems = itemRepository.findAllByNameIsContaining(pageable, search);
        } else {
            return null;
        }
        return searchItems.map(this::mapItemItemDto);
    }

    @Override
    public List<ItemDto> getItemsByMemberId(Long memberId) {
        return itemRepository.findAllByMembers_Id(memberId).stream().map(this::mapItemItemDto).collect(Collectors.toList());
    }

    private ItemDto mapItemItemDto(Item item) {
        ItemDto itemDto = modelMapper.map(item, ItemDto.class);

        itemDto.setSeller(item.getMembers());

        if (item.getImagePath() == null) {
            itemDto.setImagePath(null);
        } else {
            String[] pathArray = item.getImagePath().split(",");
            itemDto.setImagePath(pathArray);
        }

        return itemDto;
    }

    public List<Item> getItemInTimeOutAuction(){
        return itemRepository.findAllInTimeOut(LocalDateTime.now());
    }

    public void setIsEnd(Long itemId, boolean isEnd){
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new CItemNotFoundException("해당 item이 존재하지 않습니다."));
        item.setEnd(isEnd);
        itemRepository.save(item);
    }
}
