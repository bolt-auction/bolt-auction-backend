package com.neoga.boltacution.security.dto;

import lombok.*;

@Builder @NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class LoginDto {
    private String email;
    private String passwd;
}
