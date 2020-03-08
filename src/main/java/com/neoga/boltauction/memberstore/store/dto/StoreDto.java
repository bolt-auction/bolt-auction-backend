package com.neoga.boltauction.memberstore.store.dto;

import lombok.Data;

@Data
public class StoreDto {
    private Long memberId;
    private String description;
    private String storeName;
    private String imagePath;
}
