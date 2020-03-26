package com.neoga.platform.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDto {
    @JsonProperty("orderId")
    private Long id;
    private Long memberId;
    private Long itemId;
    private int price;
    private LocalDateTime createDt;
}
