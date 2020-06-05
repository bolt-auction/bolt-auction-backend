package com.neoga.communication.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.core.Relation;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Relation(collectionRelation = "chatRoomList")
public class ChatRoomDto {
    @JsonProperty("chatRoomId")
    private Long id;
    @JsonProperty("chatRoomName")
    private String name;
    private LocalDateTime createDt;
    private ItemDto item;
}
