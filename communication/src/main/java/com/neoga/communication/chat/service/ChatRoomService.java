package com.neoga.communication.chat.service;


import com.neoga.communication.chat.domain.ChatRoom;
import com.neoga.communication.chat.domain.ChatRoomJoin;
import com.neoga.communication.chat.dto.CreateRoomRequestDto;
import com.neoga.communication.chat.repository.ChatRoomJoinRepository;
import com.neoga.communication.chat.repository.ChatRoomRepository;
import com.neoga.communication.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final AuthService authService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinRepository chatjoinRepository;


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
}
