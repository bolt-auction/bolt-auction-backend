package com.neoga.boltacution.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginUserDetail{
    private String email;
    private String name;
}
