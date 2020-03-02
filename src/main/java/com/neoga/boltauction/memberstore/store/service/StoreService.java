package com.neoga.boltauction.memberstore.store.service;

import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.dto.StoreDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoreService {

    StoreDto updateStore(Store store, String description, String memberName, MultipartFile image) throws IOException;

    StoreDto getStore(Long storeId);
}
