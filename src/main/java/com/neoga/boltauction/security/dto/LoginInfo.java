package com.neoga.boltauction.security.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginInfo {
    private Long member_id;
    private String email;
    private String name;
    private List<String> role;
}
