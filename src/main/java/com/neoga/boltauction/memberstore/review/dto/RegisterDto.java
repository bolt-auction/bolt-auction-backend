package com.neoga.boltauction.memberstore.review.dto;

import lombok.Data;
import net.minidev.json.JSONObject;

@Data
public class RegisterDto {
    private Long id;
    private String name;
    private JSONObject imagePath;
}
