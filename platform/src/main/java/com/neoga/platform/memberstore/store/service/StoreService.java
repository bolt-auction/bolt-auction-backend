package com.neoga.platform.memberstore.store.service;

import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.store.dto.StoreDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StoreService {

    StoreDto updateStore(Members members, String description, String memberName, MultipartFile image) throws IOException;

    StoreDto getStore(Long storeId);
}
