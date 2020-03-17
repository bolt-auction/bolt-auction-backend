package com.neoga.platform.chat.service;

import com.neoga.platform.chat.domain.ChatRoom;
import com.neoga.platform.chat.domain.ChatRoomJoin;
import com.neoga.platform.chat.dto.CreateRoomRequestDto;
import com.neoga.platform.chat.repository.ChatRoomJoinRepository;
import com.neoga.platform.chat.repository.ChatRoomRepository;
import com.neoga.platform.exception.custom.CMemberNotFoundException;
import com.neoga.platform.item.domain.Item;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.member.repository.MemberRepository;
import com.neoga.platform.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final EntityManager em;
    private final AuthService authService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinRepository chatjoinRepository;
    private final MemberRepository memberRepository;


    // 1:1 채팅방 생성
    public ChatRoom createChatRoom(CreateRoomRequestDto createRoomDto) {
        log.info(createRoomDto.getName() + " 채팅방 생성");
        Item item = em.getReference(Item.class, createRoomDto.getItemId());
        ChatRoom newRoom = chatRoomRepository
                .save(ChatRoom.builder()
                        .name(createRoomDto.getName())
                        .item(item).build());

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
        Members member = memberRepository.findById(memberId).orElseThrow(CMemberNotFoundException::new);
        ChatRoomJoin roomJoin = ChatRoomJoin.builder()
                .participant(member)
                .chatRoom(room).build();
        chatjoinRepository.save(roomJoin);
    }
}
