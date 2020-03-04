package com.neoga.boltauction.chat.service;

import com.neoga.boltauction.chat.domain.ChatRoom;
import com.neoga.boltauction.chat.domain.ChatRoomJoin;
import com.neoga.boltauction.chat.repository.ChatRoomJoinRepository;
import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import com.neoga.boltauction.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatRoomJoinService {
    private final ChatRoomJoinRepository chatjoinRepository;
    private final MemberRepository memberRepository;
    private final AuthService authService;

    //채팅방 참여
    public void joinRoom(ChatRoom Room, Long memberId){
        Members member = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
        ChatRoomJoin roomJoin = ChatRoomJoin.builder()
                .participant(member)
                .chatRoom(Room).build();
        chatjoinRepository.save(roomJoin);
    }
}
