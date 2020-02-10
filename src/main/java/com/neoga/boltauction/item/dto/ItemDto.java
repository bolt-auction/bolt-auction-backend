package com.neoga.boltauction.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ItemDto {

    private Long itemId;
    private String itemName;
    private String description;
    private int quickPrice;
    private int startPrice;
    private int minBidPrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime createDt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime endDt;
    private Long categoryId;
    //private Long memberId;
}
