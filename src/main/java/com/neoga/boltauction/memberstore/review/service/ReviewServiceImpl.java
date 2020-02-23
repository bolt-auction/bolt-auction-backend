package com.neoga.boltauction.memberstore.review.service;

import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.exception.custom.CStoreNotFoundException;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import com.neoga.boltauction.memberstore.review.domain.Review;
import com.neoga.boltauction.memberstore.review.domain.ReviewDto;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public ReviewDto addReview(Long storeId, Long memberId, String content) {
        Store findStore = storeRepository.findById(storeId).orElseThrow(CStoreNotFoundException::new);
        Members findMember = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
        Review review = new Review();

        review.setRegister(findMember);
        review.setStore(findStore);
        review.setContent(content);
        review.setCreateDt(LocalDateTime.now());

    }


    private ReviewDto mapReviewReviewDto(Review review) {
        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);

        return reviewDto;
    }
}
