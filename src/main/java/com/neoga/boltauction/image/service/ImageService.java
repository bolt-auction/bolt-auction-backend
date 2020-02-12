package com.neoga.boltauction.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String saveItemImages(Long itemId, MultipartFile... images) throws IOException;
    String updateItemImages(Long itemId, MultipartFile... images) throws IOException;
    String saveStoreImage(Long storeId, MultipartFile image) throws IOException;
}
