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
    public Store saveStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Store updateStore(Long storeId, String description, MultipartFile image) throws IOException {
        Store store = storeRepository.findById(storeId).orElseThrow(CStoreNotFoundException::new);

        store.setDescription(description);
        String imagePath = imageService.saveStoreImage(storeId, image);
        store.setImagePath(imagePath);
        return storeRepository.save(store);
    }
}
