package com.neoga.boltauction.memberstore.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    @GetMapping("/store/{store-id}")
    public void getReviews(@PathVariable(name = "store-id") Long storeId) {

    }

    @PostMapping("/store/{store-id}")
    public void addReview(@PathVariable(name = "store-id") Long storeId) {

    }

    @DeleteMapping("/store/{store-id}")
    public void deleteReview(@PathVariable(name = "store-id") Long storeId) {

    }

}
