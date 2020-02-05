package com.neoga.boltacution.security.service;


import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import com.neoga.boltacution.security.domain.SecurityMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepo;

    public UserDetails loadUserByUsername(String userEmail) {
        Optional<Members> optional = memberRepo.findByEmail(userEmail);

        if(!optional.isPresent()){
            throw new UsernameNotFoundException(userEmail + " 사용자 없음");
        }else{
            Members member = optional.get();
            return new SecurityMember(member);
        }
    }
}