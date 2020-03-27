package com.neoga.platform.communication.chat.repository;

import com.neoga.platform.communication.chat.domain.ChatRoomJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {
}
