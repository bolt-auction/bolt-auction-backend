package com.neoga.boltacution.security.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.security.util.SecurityUtil;
import org.springframework.security.core.userdetails.User;


public class SecurityMember extends User {
    private Members member;

    public SecurityMember(Members member) {
        super(member.getEmail(), member.getPasswd(), SecurityUtil.authorities(member.getRole()));
        this.member = member;
    }

    public Members getMember() {
        return member;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isEnabled() {
        return true;
    }
}
