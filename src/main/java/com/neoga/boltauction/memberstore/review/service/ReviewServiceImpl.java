package com.neoga.boltauction.memberstore.review.service;

import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.exception.custom.CReviewNotExistException;
import com.neoga.boltauction.exception.custom.CStoreNotFoundException;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import com.neoga.boltauction.memberstore.review.domain.RegisterDto;
import com.neoga.boltauction.memberstore.review.domain.Review;
import com.neoga.boltauction.memberstore.review.domain.ReviewDto;
import com.neoga.boltauction.memberstore.review.repository.ReviewRepository;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    @Override
    public ReviewDto addReview(Long storeId, Long memberId, String content) {
        Store findStore = storeRepository.findById(storeId).orElseThrow(CStoreNotFoundException::new);
        Members findMember = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
        Review review = new Review();

        review.setRegister(findMember);
        review.setStore(findStore);
        review.setContent(content);
        review.setCreateDt(LocalDateTime.now());
        reviewRepository.save(review);

        return mapReviewReviewDto(review);
    }

    @Override
    public List<ReviewDto> getReviews(Long storeId) {
        List<Review> reviewList = reviewRepository.findAllByStore_IdOrderByCreateDtDesc(storeId);

        return reviewList.stream().map(review -> mapReviewReviewDto(review)).collect(Collectors.toList());
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new CReviewNotExistException("해당 리뷰가 존재하지 않습니다."));
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ReviewDto mapReviewReviewDto(Review review) {
        ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);

        RegisterDto registerDto = new RegisterDto();
        registerDto.setId(review.getRegister().getId());
        registerDto.setName(review.getRegister().getName());

        return reviewDto;
    }
}
