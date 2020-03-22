package com.neoga.communication.chat.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Item {
    private Long id;
    private String name;
    private String description;
    private int quickPrice;
    private int startPrice;
    private int minBidPrice;
    private int currentPrice;
    private boolean isSell = false;
    private LocalDateTime createDt;
    private LocalDateTime endDt;
    private Long categoryId;
    private Long memberId;
    private String imagePath;
    private int bidCount;
}
