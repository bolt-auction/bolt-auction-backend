package com.neoga.communication.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.neoga.communication.chat.domain.ChatRoom;
import com.neoga.communication.chat.util.ChatRoomSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.core.Relation;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Relation(collectionRelation = "chatMessageList")
public class ChatMessageDto {
    @JsonProperty("chatMessageId")
    private Long id;
    @JsonProperty("chatMessageContent")
    private String content;
    private LocalDateTime createDt;
    private boolean isRead = false;
    @JsonSerialize(using = ChatRoomSerializer.class)
    private ChatRoom chatRoom;
    private MemberDto sender;
}
