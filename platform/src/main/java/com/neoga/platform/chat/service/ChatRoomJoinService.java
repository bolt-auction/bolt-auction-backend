package com.neoga.platform.chat.service;

import com.neoga.platform.chat.domain.ChatRoom;
import com.neoga.platform.chat.domain.ChatRoomJoin;
import com.neoga.platform.chat.repository.ChatRoomJoinRepository;
import com.neoga.platform.exception.custom.CMemberNotFoundException;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.member.repository.MemberRepository;
import com.neoga.platform.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatRoomJoinService {
    private final ChatRoomJoinRepository chatjoinRepository;
    private final MemberRepository memberRepository;
    private final AuthService authService;

    //채팅방 참여
    public void joinRoom(ChatRoom Room, Long memberId) {
        Members member = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
        ChatRoomJoin roomJoin = ChatRoomJoin.builder()
                .participant(member)
                .chatRoom(Room).build();
        chatjoinRepository.save(roomJoin);
    }
}
