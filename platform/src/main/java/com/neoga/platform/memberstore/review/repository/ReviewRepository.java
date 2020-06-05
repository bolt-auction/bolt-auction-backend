package com.neoga.platform.memberstore.review.repository;

import com.neoga.platform.memberstore.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByStore_IdOrderByCreateDtDesc(Long storeId);
}
