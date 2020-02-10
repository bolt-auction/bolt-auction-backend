package com.neoga.boltacution.security.dto;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginUserDetailDto {
    private Long member_id;
    private String email;
    private String name;
    private String accessToken;
    private String tokenType;
}
