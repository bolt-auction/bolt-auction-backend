package com.neoga.platform.image.service;

import com.neoga.platform.item.domain.Item;
import com.neoga.platform.memberstore.member.domain.Members;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    void saveItemImages(Item itemId, MultipartFile... images) throws IOException;

    void updateItemImages(Item item, MultipartFile... images) throws IOException;

    void saveStoreImage(Members members, MultipartFile image) throws IOException;
}
