package com.neoga.boltauction.bid.dto;

import lombok.Data;

@Data
public class BidDto {
    private Long bidId;
    private Register member;
    private Long itemId;
    private int price;
}
