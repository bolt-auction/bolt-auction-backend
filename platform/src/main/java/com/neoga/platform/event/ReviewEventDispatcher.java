package com.neoga.platform.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ReviewEventDispatcher {
    private final RabbitTemplate rabbitTemplate;
    @Value("${platform.exchange}")
    private String platformExchange;
    @Value("${platform.review.add.key}")
    private String platformSolvedRoutingKey;
    private final ObjectMapper objectMapper;

    public void send(Long receiverId, Long registerId, String content, LocalDateTime createDt) throws JsonProcessingException {
        ReviewAddEvent reviewAddEvent = new ReviewAddEvent(
                receiverId,
                registerId,
                content,
                createDt
        );

        rabbitTemplate.convertAndSend(
                platformExchange,
                platformSolvedRoutingKey,
                reviewAddEvent);
    }
}
