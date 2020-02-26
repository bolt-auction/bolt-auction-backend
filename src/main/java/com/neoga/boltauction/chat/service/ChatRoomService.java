package com.neoga.boltauction.chat.service;

import com.neoga.boltauction.chat.domain.ChatRoom;
import com.neoga.boltauction.chat.repository.ChatRoomRepository;
import com.neoga.boltauction.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinService chatRoomJoinService;
    private final AuthService authService;

    // 1:1 채팅방 생성
    public ChatRoom createChatRoom(String roomName, Long recvMemberId){
        ChatRoom newRoom = chatRoomRepository.save(ChatRoom.builder().name(roomName).build());
        Long memberId = authService.getLoginInfo().getMemberId();
        chatRoomJoinService.joinRoom(newRoom, recvMemberId);
        chatRoomJoinService.joinRoom(newRoom, memberId);
        return newRoom;
    }

    //자신의 채팅방참여 리스트 반환
    public List<ChatRoom> findChatRoomList(){
        Long memberId = authService.getLoginInfo().getMemberId();
        List<ChatRoom> chatList = chatRoomRepository.findChatRoomList(memberId);
        return chatList;
    }
}
