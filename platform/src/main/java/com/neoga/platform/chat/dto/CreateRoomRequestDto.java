package com.neoga.platform.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomRequestDto {
    @JsonProperty("chatRoomName")
    private String name;
    @JsonProperty("recvMemberId")
    private Long rcvMemberId;
    private Long itemId;
}
