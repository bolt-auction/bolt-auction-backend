package com.neoga.boltauction.security.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginInfo {
    private Long memberId;
    private Long storeId;
    private String uid;
    private String name;
    private List<String> role;
}
