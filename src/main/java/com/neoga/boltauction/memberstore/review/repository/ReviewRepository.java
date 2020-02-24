package com.neoga.boltauction.memberstore.review.repository;

import com.neoga.boltauction.memberstore.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByStore_IdOrderByCreateDtDesc(Long storeId);
}
