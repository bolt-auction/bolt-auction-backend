package com.neoga.platform.bid.dto;

import lombok.Data;

@Data
public class Register {
    private Long memberId;
    private String memberName;
    private String memberImagePath;
}