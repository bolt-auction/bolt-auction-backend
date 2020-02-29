package com.neoga.boltauction.memberstore.store.service;

import com.neoga.boltauction.exception.custom.CStoreNotFoundException;
import com.neoga.boltauction.image.service.ImageService;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.dto.StoreDto;
import com.neoga.boltauction.memberstore.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Override
    public StoreDto updateStore(Store store, String description, String memberName, MultipartFile image) throws IOException {

        store.setDescription(description);
        store.getMembers().setName(memberName);
        imageService.saveStoreImage(store, image);

        Store saveStore = storeRepository.save(store);

        return mapStoreStoreDto(saveStore);
    }

    @Override
    public StoreDto getStore(Long storeId) {
        Store findStore = storeRepository.findById(storeId).orElseThrow(CStoreNotFoundException::new);

        return mapStoreStoreDto(findStore);
    }

    private StoreDto mapStoreStoreDto(Store store) {
        StoreDto storeDto = modelMapper.map(store, StoreDto.class);
        storeDto.setStoreName(store.getMembers().getName());

        return storeDto;
    }
}
