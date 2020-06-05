package com.neoga.communication.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.neoga.communication.chat.util.MemberDtoDeserializer;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@JsonDeserialize(using = MemberDtoDeserializer.class)
public class MemberDto {
    private Long memberId;
    private String memberName;
    private String imagePath;
}
