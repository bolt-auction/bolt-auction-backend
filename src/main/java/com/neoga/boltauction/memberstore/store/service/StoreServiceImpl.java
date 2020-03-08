package com.neoga.boltauction.memberstore.store.service;

import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.image.service.ImageService;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import com.neoga.boltauction.memberstore.store.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final MemberRepository memberRepository;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Override
    public StoreDto updateStore(Members members, String description, String memberName, MultipartFile image) throws IOException {

        members.setDescription(description);
        members.setStoreName(memberName);
        imageService.saveStoreImage(members, image);

        Members saveMember = memberRepository.save(members);

        return mapMemberStoreDto(saveMember);
    }

    @Override
    public StoreDto getStore(Long memberId) {
        Members findMember = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);

        return mapMemberStoreDto(findMember);
    }

    private StoreDto mapMemberStoreDto(Members members) {
        StoreDto storeDto = modelMapper.map(members, StoreDto.class);
        storeDto.setMemberId(members.getId());

        return storeDto;
    }
}
