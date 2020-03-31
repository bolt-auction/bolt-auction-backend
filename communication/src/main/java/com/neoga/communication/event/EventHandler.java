package com.neoga.communication.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoga.communication.notification.domain.NotifyType;
import com.neoga.communication.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventHandler {
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${platform.queue}")
    void handleReviewAdd(final ReviewAddEvent reviewEvent) throws IOException {
        log.info("Review Add Event 수신: {}", reviewEvent.getContent());

        try {
            notificationService.sendToUser(
                    NotifyType.REVIEW,
                    reviewEvent.getContent(),
                    reviewEvent.getReceiverId()
            );
        } catch (final Exception e) {
            log.error("ReviewAddEvent 처리 시 에러", e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
