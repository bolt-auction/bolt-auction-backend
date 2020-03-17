package com.neoga.platform.memberstore.review.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private Long reviewId;
    private String content;
    private RegisterDto register;
    private LocalDateTime createDt;
}
