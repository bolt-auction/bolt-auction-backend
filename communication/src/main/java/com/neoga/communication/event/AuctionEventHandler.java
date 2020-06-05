package com.neoga.communication.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoga.communication.notification.domain.NotifyType;
import com.neoga.communication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuctionEventHandler {
    private final NotificationService notificationService;

    @RabbitListener(queues = "${platform.queue}")
    void handleReviewAdd(final AuctionEndEvent auctionEndEvent) throws IOException {
        log.info("[Auction end Event 수신] itemId: {}, buyerId: {}", auctionEndEvent.getItemId(), auctionEndEvent.getBuyerId());

        String sellerMsg = auctionEndEvent.getPrice()+"원 낙찰되셨습니다.";
        String buyerMsg = "경매종료되었습니다.";

        try {
            notificationService.sendToUser(
                    NotifyType.AUCTION_END,
                    buyerMsg,
                    auctionEndEvent.getBuyerId()
            );
            notificationService.sendToUser(
                    NotifyType.AUCTION_END,
                    sellerMsg,
                    auctionEndEvent.getSellerId()
            );
        } catch (final Exception e) {
            log.error("ReviewAddEvent 처리 시 에러", e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
