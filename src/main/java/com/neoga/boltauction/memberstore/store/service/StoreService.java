package com.neoga.boltauction.memberstore.store.service;

import com.neoga.boltauction.memberstore.store.domain.Store;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoreService {
    Store saveStore(Store store);

    Store updateStore(Store store, String description, MultipartFile image) throws IOException;

    Store getStore(Long storeId);
}
