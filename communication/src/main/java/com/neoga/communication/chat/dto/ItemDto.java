package com.neoga.communication.chat.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemDto {
    @JsonProperty("itemId")
    private Long id;
    @JsonProperty("itemName")
    private String name;
    private String[] imagePath;
}
