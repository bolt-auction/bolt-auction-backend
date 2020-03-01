package com.neoga.boltauction.chat.service;

import com.neoga.boltauction.chat.domain.ChatMessage;
import com.neoga.boltauction.chat.domain.ChatRoom;
import com.neoga.boltauction.chat.dto.SendMessageDto;
import com.neoga.boltauction.chat.repository.ChatMessageRepository;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    private final EntityManager em;

    public ChatMessage saveMessage(SendMessageDto sendMessageDto){
        Members sender = em.getReference(Members.class, authService.getLoginInfo().getMemberId());
        ChatRoom chatRoom = em.getReference(ChatRoom.class,sendMessageDto.getChatRoomId());
        ChatMessage chatMessage = modelMapper.map(sendMessageDto, ChatMessage.class);
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setSender(sender);

        chatMessageRepository.save(chatMessage);

        return  chatMessage;
    }


    public Page<ChatMessage> findMessageByChatRoom(ChatRoom chatroom, Pageable pageable){
        return chatMessageRepository.findAllByChatRoom(chatroom, pageable);
    }

}
