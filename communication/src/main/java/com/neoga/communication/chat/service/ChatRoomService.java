package com.neoga.communication.chat.service;


import com.neoga.communication.chat.domain.ChatMessage;
import com.neoga.communication.chat.domain.ChatRoom;
import com.neoga.communication.chat.domain.ChatRoomJoin;
import com.neoga.communication.chat.dto.*;
import com.neoga.communication.chat.repository.ChatRoomJoinRepository;
import com.neoga.communication.chat.repository.ChatRoomRepository;
import com.neoga.communication.client.ItemClient;
import com.neoga.communication.client.MemberClient;
import com.neoga.communication.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final AuthService authService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinRepository chatjoinRepository;
    private final ModelMapper modelMapper;
    private final ItemClient itemClient;

    // 1:1 채팅방 생성
    public ChatRoom createChatRoom(CreateRoomRequestDto createRoomDto) {
        log.info(createRoomDto.getName() + " 채팅방 생성");
        ChatRoom newRoom = chatRoomRepository
                .save(ChatRoom.builder()
                        .name(createRoomDto.getName())
                        .itemId(createRoomDto.getItemId()).build());

        Long memberId = authService.getLoginInfo().getMemberId();

        joinRoom(newRoom, createRoomDto.getRcvMemberId());
        joinRoom(newRoom, memberId);

        return newRoom;
    }

    //자신이 참여한 채팅방 리스트 반환
    public Page<ChatRoom> findChatRoomList(Pageable pageable) {
        Long memberId = authService.getLoginInfo().getMemberId();
        return chatRoomRepository.findChatRoomList(memberId, pageable);
    }

    //특정 채팅방 참여
    public void joinRoom(ChatRoom room, Long memberId) {
        log.info("meberId: " + memberId + "님 " + room.getId() + "번 채팅방 참여");
        ChatRoomJoin roomJoin = ChatRoomJoin.builder()
                .memberId(memberId)
                .chatRoom(room).build();
        chatjoinRepository.save(roomJoin);
    }

    public Page<ChatRoomDto> mapChatRoomIntoDtoPage(Page<ChatRoom> chatMessagePage) {
        Page<ChatRoomDto> chatRoomDtoPage = chatMessagePage.map(new Function<ChatRoom, ChatRoomDto>() {
            @Override
            public ChatRoomDto apply(ChatRoom chatRoom) {
                ItemDto itemDto = itemClient.retrieveItemById(chatRoom.getItemId());
                ChatRoomDto chatRoomDto = new ChatRoomDto();
                chatRoomDto.setId(chatRoom.getId());
                chatRoomDto.setName(chatRoom.getName());
                chatRoomDto.setCreateDt(chatRoom.getCreateDt());
                chatRoomDto.setItem(itemDto);

                return chatRoomDto;
            }});

        return chatRoomDtoPage;
    }
}
