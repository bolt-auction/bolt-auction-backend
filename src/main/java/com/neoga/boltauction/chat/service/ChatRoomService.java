package com.neoga.boltauction.chat.service;

import com.neoga.boltauction.chat.domain.ChatRoom;
import com.neoga.boltauction.chat.dto.CreateRoomRequestDto;
import com.neoga.boltauction.chat.repository.ChatRoomRepository;
import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinService chatRoomJoinService;
    private final AuthService authService;
    private final EntityManager em;

    // 1:1 채팅방 생성
    public ChatRoom createChatRoom(CreateRoomRequestDto createRoomDto){
        Item item = em.getReference(Item.class, createRoomDto.getItemId());
        ChatRoom newRoom = chatRoomRepository
                .save(ChatRoom.builder()
                        .name(createRoomDto.getName())
                        .item(item).build());

        Long memberId = authService.getLoginInfo().getMemberId();

        chatRoomJoinService.joinRoom(newRoom, createRoomDto.getRcvMemberId());
        chatRoomJoinService.joinRoom(newRoom, memberId);
        return newRoom;
    }

    //자신의 채팅방참여 리스트 반환
    public List<ChatRoom> findChatRoomList(Pageable pageable){
        Long memberId = authService.getLoginInfo().getMemberId();
        List<ChatRoom> chatList = chatRoomRepository.findChatRoomList(memberId, pageable);
        return chatList;
    }
}
