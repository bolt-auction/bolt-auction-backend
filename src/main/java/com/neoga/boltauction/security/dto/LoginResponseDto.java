package com.neoga.boltauction.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private Long member_id;
    private String uid;
    private String name;
    private String accessToken;
    private String tokenType;
}
