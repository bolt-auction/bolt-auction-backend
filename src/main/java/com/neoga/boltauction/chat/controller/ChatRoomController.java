package com.neoga.boltauction.chat.controller;

import com.neoga.boltauction.chat.domain.ChatRoom;
import com.neoga.boltauction.chat.domain.ChatRoomJoin;
import com.neoga.boltauction.chat.repository.ChatRoomRepository;
import com.neoga.boltauction.chat.service.ChatRoomJoinService;
import com.neoga.boltauction.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomJoinService chatRoomJoinService;

    // 자신의 채팅방 목록 반환
    @GetMapping()
    @ResponseBody
    public ResponseEntity findRoomInMember() {
        List<ChatRoom> roomJoin = chatRoomService.findChatRoomList();
        return ResponseEntity.ok().body(roomJoin);
    }

    // 1:1 채팅방 생성
    @PostMapping()
    @ResponseBody
    public ResponseEntity createRoom(@RequestParam String roomName, @RequestParam Long rcvMemberId) {
        ChatRoom newRoom = chatRoomService.createChatRoom(roomName, rcvMemberId);
        return ResponseEntity.ok().body(newRoom);
    }

    // 특정 채팅방 조회
    @GetMapping("/{roomId}")
    @ResponseBody
    public ResponseEntity roomInfo(@PathVariable Long roomId) {
        return ResponseEntity.ok().build();
    }
}
