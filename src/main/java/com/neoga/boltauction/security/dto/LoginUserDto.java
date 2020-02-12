package com.neoga.boltauction.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginUserDto {
    private Long member_id;
    private String email;
    private String name;
    private String accessToken;
    private String tokenType;
}
