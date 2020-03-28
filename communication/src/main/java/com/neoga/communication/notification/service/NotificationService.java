package com.neoga.communication.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoga.communication.notification.domain.Notification;
import com.neoga.communication.notification.domain.NotifyType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;


@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final ObjectMapper objectMapper;
    private final EntityManager em;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final RabbitTemplate rabbitTemplate;

    public void sendToUser(NotifyType type, String content, Long receiverId) throws JsonProcessingException {
        Notification notification = Notification.builder()
                .type(type)
                .content(content)
                .receiverId(receiverId).build();

        log.info("[send notification] recevierId: {}", receiverId);

        String msg = objectMapper.writeValueAsString(notification);
        rabbitTemplate.convertAndSend("amq.topic","notification."+receiverId,msg);
    }
}
