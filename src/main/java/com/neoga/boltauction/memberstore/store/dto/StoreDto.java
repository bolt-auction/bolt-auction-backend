package com.neoga.boltauction.memberstore.store.dto;

import com.neoga.boltauction.item.domain.Item;
import com.neoga.boltauction.memberstore.review.domain.Review;
import lombok.Data;
import net.minidev.json.JSONObject;

import java.util.List;

@Data
public class StoreDto {
    private Long id;
    private String description;
    private String name;
    private JSONObject imagePath;
}
