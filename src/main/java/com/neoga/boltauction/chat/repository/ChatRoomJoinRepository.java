package com.neoga.boltauction.chat.repository;

import com.neoga.boltauction.chat.domain.ChatRoomJoin;
import com.neoga.boltauction.memberstore.member.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {
    @Query("select j.participant from ChatRoomJoin j JOIN j.participant ON j.participant.id = :memberId")
    Optional<Members> getChatMemberInfo(Long chatRoomid);
}
