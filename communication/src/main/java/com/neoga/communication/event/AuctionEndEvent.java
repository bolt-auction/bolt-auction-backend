package com.neoga.communication.event;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class AuctionEndEvent implements Serializable {
    private Long buyerId;
    private Long sellerId;
    private Long itemId;
    private int price;
}
