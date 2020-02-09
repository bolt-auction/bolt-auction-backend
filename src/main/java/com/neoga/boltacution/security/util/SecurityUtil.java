package com.neoga.boltacution.security.util;

import com.neoga.boltacution.memberstore.member.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUtil {
    public static Collection<? extends GrantedAuthority> authorities(List<String> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());
    }
}
