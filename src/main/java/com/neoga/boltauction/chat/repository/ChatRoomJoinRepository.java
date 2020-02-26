package com.neoga.boltauction.chat.repository;

import com.neoga.boltauction.chat.domain.ChatRoomJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {
}
