package com.neoga.boltauction.memberstore.review.controller;

import com.neoga.boltauction.memberstore.review.domain.Review;
import com.neoga.boltauction.memberstore.review.dto.ReviewDto;
import com.neoga.boltauction.memberstore.review.service.ReviewServiceImpl;
import com.neoga.boltauction.security.service.AuthService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final AuthService authService;
    private final ReviewServiceImpl reviewService;

    @ApiOperation(value = "상점 리뷰조회")
    @ApiImplicitParam(name = "storeId", value = "상품 id", dataType = "long")
    @GetMapping("/store/{store-id}")
    public ResponseEntity getReviews(@PathVariable(name = "store-id") Long storeId) {
        List<ReviewDto> reviews = reviewService.getReviews(storeId);

        Resources resources = new Resources(reviews);
        resources.add(linkTo(Review.class).slash("store/" + storeId).withSelfRel());
        resources.add(new Link("/swagger-ui.html#/review-controller/getReviewsUsingGET").withRel("profile"));

        return ResponseEntity.ok(resources);
    }

    @ApiOperation(value = "상점 리뷰 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "상품 id", dataType = "long"),
            @ApiImplicitParam(name = "content", value = "리뷰 내용", dataType = "string")
    })
    @PostMapping("/store/{store-id}")
    public ResponseEntity addReview(@PathVariable(name = "store-id") Long storeId, String content) {
        Long memberId = authService.getLoginInfo().getMemberId();
        ReviewDto reviewDto = reviewService.addReview(storeId, memberId, content);

        Resource resource = new Resource(reviewDto);
        resource.add(linkTo(ReviewController.class).slash("store/" + storeId).withSelfRel());
        resource.add(new Link("/swagger-ui.html#/review-controller").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @ApiOperation(value = "리뷰 삭제")
    @ApiImplicitParam(name = "review-id", value = "리뷰 id", dataType = "long")
    @DeleteMapping("{review-id}")
    public ResponseEntity deleteReview(@PathVariable(name = "review-id") Long reviewId) {
        Long memberId = authService.getLoginInfo().getMemberId();
        Review findReview = reviewService.getReview(reviewId);

        if (memberId != findReview.getRegister().getId()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        reviewService.deleteReview(reviewId);

        return ResponseEntity.ok().build();
    }

}
