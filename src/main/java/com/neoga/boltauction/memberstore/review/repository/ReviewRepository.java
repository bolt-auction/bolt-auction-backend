package com.neoga.boltauction.memberstore.review.repository;

import com.neoga.boltauction.memberstore.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
