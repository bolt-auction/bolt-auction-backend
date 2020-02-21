package com.neoga.boltauction.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private Long memberId;
    private Long storeId;
    private String uid;
    private String name;
    private String accessToken;
    private String tokenType;
}
