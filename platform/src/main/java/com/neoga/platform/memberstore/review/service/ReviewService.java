package com.neoga.platform.memberstore.review.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.neoga.platform.memberstore.review.domain.Review;
import com.neoga.platform.memberstore.review.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    ReviewDto addReview(Long storeId, Long memberId, String content) throws JsonProcessingException;

    List<ReviewDto> getReviews(Long storeId);

    Review getReview(Long reviewId);

    void deleteReview(Long reviewId);
}
