package com.neoga.communication.security.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginInfo {
    private Long memberId;
    private String uid;
    private String name;
    private List<String> role;
}
