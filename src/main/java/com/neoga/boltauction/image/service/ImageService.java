package com.neoga.boltauction.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    public String saveItemImages(Long itemId, MultipartFile... images) throws IOException;
}
