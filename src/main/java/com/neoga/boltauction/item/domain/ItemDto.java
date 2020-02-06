package com.neoga.boltauction.item.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemDto {

    private Long itemId;
    private String itemName;
    private String description;
    private int quickPrice;
    private int startPrice;
    private int minBidPrice;
    private LocalDateTime createDate;
    private LocalDateTime endDate;
    private Long categoryId;
    //private Long memberId;
}
