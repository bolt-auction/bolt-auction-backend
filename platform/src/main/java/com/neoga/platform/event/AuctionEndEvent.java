package com.neoga.platform.event;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class AuctionEndEvent {
    private Long buyerId;
    private Long sellerId;
    private Long itemId;
    private int price;
    private LocalDateTime createDt;
}
