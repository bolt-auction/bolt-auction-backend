package com.neoga.platform.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.neoga.platform.order.domain.Orders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuctionEventDispatcher {
    private final RabbitTemplate rabbitTemplate;
    @Value("${platform.exchange}")
    private String platformExchange;
    @Value("${platform.auction.end.key}")
    private String platformAuctionRoutingKey;

    public void send(Long buyerId, Long sellerId,Long itemId, int price, LocalDateTime createDt) throws JsonProcessingException {
        AuctionEndEvent auctionEndEvent = new AuctionEndEvent(
                buyerId,
                sellerId,
                itemId,
                price,
                createDt
        );

        rabbitTemplate.convertAndSend(
                platformExchange,
                platformAuctionRoutingKey,
                auctionEndEvent);

        log.info("[auction event publish] itemId: {}, buyerId: {}",itemId, buyerId);
    }
}