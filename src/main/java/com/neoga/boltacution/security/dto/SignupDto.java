package com.neoga.boltacution.security.dto;

import lombok.*;

@Builder @NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class SignupDto {
    private String email;
    private String passwd;
    private String name;
}
