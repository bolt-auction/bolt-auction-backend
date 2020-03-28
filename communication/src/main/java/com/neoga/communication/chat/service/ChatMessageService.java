package com.neoga.communication.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoga.communication.chat.domain.ChatMessage;
import com.neoga.communication.chat.domain.ChatRoom;
import com.neoga.communication.chat.dto.ChatMessageDto;
import com.neoga.communication.chat.dto.MemberDto;
import com.neoga.communication.chat.dto.SendMessageDto;
import com.neoga.communication.chat.repository.ChatMessageRepository;
import com.neoga.communication.client.MemberClient;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final EntityManager em;
    private final RabbitTemplate rabbitTemplate;
    private final MemberClient memberClient;
    private final ChatMessageRepository chatMessageRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public void sendMessage(Long chatRoomId, SendMessageDto sendMessageDto) throws JsonProcessingException {
        ChatRoom chatRoom = em.getReference(ChatRoom.class, chatRoomId);

        ChatMessage chatMessage = modelMapper.map(sendMessageDto, ChatMessage.class);
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSenderId(sendMessageDto.getSenderId());

        ChatMessage newMessage = chatMessageRepository.save(chatMessage);

        String msg = objectMapper.writeValueAsString(newMessage);
        rabbitTemplate.convertAndSend("amq.topic", "chatRoom." + chatRoomId, msg);
    }

    public Page<ChatMessage> findMessageByChatRoom(Long chatRoomId, Pageable pageable) {
        ChatRoom chatroom = em.getReference(ChatRoom.class, chatRoomId);
        return chatMessageRepository.findAllByChatRoom(chatroom, pageable);
    }

    public Page<ChatMessageDto> mapChatIntoDtoPage(Page<ChatMessage> chatMessagePage) {
        Page<ChatMessageDto> chatMessageDtoPage = chatMessagePage.map(new Function<ChatMessage, ChatMessageDto>() {
            @Override
            public ChatMessageDto apply(ChatMessage chatMessage) {
                ChatMessageDto chatMessageDto = new ChatMessageDto();
                modelMapper.map(chatMessage, ChatMessage.class);
                chatMessageDto.setId(chatMessage.getId());
                chatMessageDto.setCreateDt(chatMessage.getCreateDt());
                chatMessageDto.setChatRoom(chatMessage.getChatRoom());
                MemberDto memberDto = memberClient.retrieveMemberById(chatMessage.getSenderId());
                chatMessageDto.setSender(memberDto);
                return chatMessageDto;
            }});

        return chatMessageDtoPage;
    }
}
