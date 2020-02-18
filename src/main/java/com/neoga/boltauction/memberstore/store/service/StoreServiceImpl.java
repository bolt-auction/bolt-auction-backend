package com.neoga.boltauction.memberstore.store.service;

import com.neoga.boltauction.exception.custom.CStoreNotFoundException;
import com.neoga.boltauction.image.service.ImageService;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ImageService imageService;

    @Override
    public Store updateStore(Store store, String description, MultipartFile image) throws IOException {

        store.setDescription(description);
        imageService.saveStoreImage(store, image);

        return storeRepository.save(store);
    }

    @Override
    public Store getStore(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(CStoreNotFoundException::new);
    }
}
