package com.neoga.boltauction.chat.service;

import com.neoga.boltauction.chat.domain.ChatMessage;
import com.neoga.boltauction.chat.domain.ChatRoom;
import com.neoga.boltauction.chat.dto.SendMessageDto;
import com.neoga.boltauction.chat.repository.ChatMessageRepository;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.lang.reflect.Member;

@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final EntityManager em;
    private final RabbitTemplate rabbitTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ModelMapper modelMapper;

    public void sendMessage(Long chatRoomId, SendMessageDto sendMessageDto){
        ChatRoom chatRoom = em.getReference(ChatRoom.class, chatRoomId);
        Members sender = em.getReference(Members.class,sendMessageDto.getSenderId());

        ChatMessage chatMessage = modelMapper.map(sendMessageDto, ChatMessage.class);
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);

        ChatMessage newMessage = chatMessageRepository.save(chatMessage);

        rabbitTemplate.convertAndSend("/topic/chatRoom."+ chatRoomId, newMessage);
    }

    public Page<ChatMessage> findMessageByChatRoom(Long chatRoomId, Pageable pageable){
        ChatRoom chatroom = em.getReference(ChatRoom.class,chatRoomId);
        return chatMessageRepository.findAllByChatRoom(chatroom, pageable);
    }
}
