package com.neoga.communication.chat.repository;

import com.neoga.communication.chat.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select r from ChatRoom r JOIN r.chatRoomJoins j ON j.memberId = :memberId")
    Page<ChatRoom> findChatRoomList(Long memberId, Pageable pageable);

}
