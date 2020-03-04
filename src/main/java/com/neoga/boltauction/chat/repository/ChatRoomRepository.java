package com.neoga.boltauction.chat.repository;

import com.neoga.boltauction.chat.domain.ChatRoom;
import com.neoga.boltauction.chat.domain.ChatRoomJoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select j.chatRoom from ChatRoomJoin j JOIN j.chatRoom  ON j.participant.id = :memberId")
    Page<ChatRoom> findChatRoomList(Long memberId, Pageable pageable);

}
