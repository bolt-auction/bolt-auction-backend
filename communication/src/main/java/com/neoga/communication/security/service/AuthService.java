package com.neoga.communication.security.service;

import com.neoga.communication.security.dto.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {
    // 저장된 인증정보에서 현재 로그인 사용자정보 조회
    public LoginInfo getLoginInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> detail = ((Map<String, Object>) authentication.getDetails());
        LoginInfo logininfo = LoginInfo.builder()
                .memberId(Long.valueOf(String.valueOf(detail.get("id"))))
                .uid(String.valueOf(detail.get("uid")))
                .name(String.valueOf(detail.get("name")))
                .role((List) detail.get("authorities")).build();
        return logininfo;
    }
}
