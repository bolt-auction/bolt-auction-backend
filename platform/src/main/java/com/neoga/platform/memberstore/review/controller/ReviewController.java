package com.neoga.platform.memberstore.review.controller;

import com.neoga.platform.memberstore.review.domain.Review;
import com.neoga.platform.memberstore.review.dto.ReviewDto;
import com.neoga.platform.memberstore.review.service.ReviewServiceImpl;
import com.neoga.platform.security.service.AuthService;
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
    @ApiImplicitParam(name = "member-id", value = "상품 id", dataType = "long")
    @GetMapping("/store/{member-id}")
    public ResponseEntity getReviews(@PathVariable(name = "member-id") Long memberId) {
        List<ReviewDto> reviewList = reviewService.getReviews(memberId);

        Resources resources = new Resources(reviewList);
        resources.add(linkTo(Review.class).slash("store/" + memberId).withSelfRel());
        resources.add(new Link("/swagger-ui.html#/review-controller/getReviewsUsingGET").withRel("profile"));

        return ResponseEntity.ok(resources);
    }

    @ApiOperation(value = "상점 리뷰 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "store-id", value = "상품 id", dataType = "long"),
            @ApiImplicitParam(name = "content", value = "리뷰 내용", dataType = "string")
    })
    @PostMapping("/store/{member-id}")
    public ResponseEntity addReview(@PathVariable(name = "member-id") Long memberId, @RequestBody String content) {
        Long currentMemberId = authService.getLoginInfo().getMemberId();
        ReviewDto review = reviewService.addReview(memberId, currentMemberId, content);

        Resource resource = new Resource(review);
        resource.add(linkTo(ReviewController.class).slash("store/" + memberId).withSelfRel());
        resource.add(new Link("/swagger-ui.html#/review-controller").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @ApiOperation(value = "리뷰 삭제")
    @ApiImplicitParam(name = "review-id", value = "리뷰 id", dataType = "long")
    @DeleteMapping("{review-id}")
    public ResponseEntity deleteReview(@PathVariable(name = "review-id") Long reviewId) {
        Long memberId = authService.getLoginInfo().getMemberId();
        Review findReview = reviewService.getReview(reviewId);

        if (findReview.getRegister().getId().equals(memberId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        reviewService.deleteReview(reviewId);

        return ResponseEntity.ok().build();
    }

}
