package com.neoga.platform.security.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequestDto {
    private String uid;
    private String passwd;
}
