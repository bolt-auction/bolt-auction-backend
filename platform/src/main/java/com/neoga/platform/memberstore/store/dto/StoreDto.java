package com.neoga.platform.memberstore.store.dto;

import lombok.Data;

@Data
public class StoreDto {
    private Long memberId;
    private String description;
    private String memberName;
    private String imagePath;
}
