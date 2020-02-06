package com.neoga.boltauction.image.service;

import com.neoga.boltauction.image.domain.ItemImage;
import com.neoga.boltauction.image.domain.MemberImage;
import com.neoga.boltauction.image.repository.ItemImageRepository;
import com.neoga.boltauction.image.repository.MemberImageRepository;
import com.neoga.boltauction.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl {

    private ItemRepository itemRepository;
    private ItemImageRepository itemImageRepository;
    private MemberImageRepository memberImageRepository;

    public void createItemImage() {

    }

    public void deleteItemImage() {

    }

    public void createMemberImage() {

    }

    public void deleteMemberImage() {

    }
}
