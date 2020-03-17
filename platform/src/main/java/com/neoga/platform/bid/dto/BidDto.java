package com.neoga.platform.bid.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class BidDto {
    private Long bidId;
    private Register member;
    private Long itemId;
    private int price;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDt;
}
