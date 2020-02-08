package com.neoga.boltacution.security.service;

import com.neoga.boltacution.exception.CEmailLoginFailedException;
import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepo;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    public String login(String email, String passwd){
        Members member = memberRepo.findByEmail(email).orElseThrow(CEmailLoginFailedException::new);

        if (!passwordEncoder.matches(passwd, member.getPasswd()))
            throw new CEmailLoginFailedException();

        String token = jwtTokenService.createToken(member.getEmail(), member.getRole());
        return token;
    }
}
