package com.neoga.platform.memberstore.review.service;

import com.neoga.platform.memberstore.review.domain.Review;
import com.neoga.platform.memberstore.review.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
    ReviewDto addReview(Long storeId, Long memberId, String content);

    List<ReviewDto> getReviews(Long storeId);

    Review getReview(Long reviewId);

    void deleteReview(Long reviewId);
}
