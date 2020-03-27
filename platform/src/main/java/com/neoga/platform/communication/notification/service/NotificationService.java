package com.neoga.platform.communication.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoga.platform.communication.notification.domain.Notification;
import com.neoga.platform.communication.notification.domain.NotifyType;
import com.neoga.platform.memberstore.member.domain.Members;
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
    private final RabbitTemplate rabbitTemplate;
    private final EntityManager em;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendToUser(NotifyType type, String content, Long receiverId) throws JsonProcessingException {
        Notification notification = Notification.builder()
                .type(type)
                .content(content)
                .receiver(em.getReference(Members.class, receiverId)).build();

        log.info("[send notification] recevierId: {}", notification.getReceiver().getId());

        String msg = objectMapper.writeValueAsString(notification);
        simpMessagingTemplate.convertAndSend("/topic/notification."+notification.getReceiver().getId(), msg);
    }
}
