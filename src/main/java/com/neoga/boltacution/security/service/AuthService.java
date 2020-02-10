package com.neoga.boltacution.security.service;

import com.neoga.boltacution.exception.custom.CEmailLoginFailedException;
import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import com.neoga.boltacution.security.dto.LoginUserDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService{

    private final MemberRepository memberRepo;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    //로그인 정보를 받아 검증 후 토큰 생성 후 로그인 정보 반환
    public LoginUserDetailDto login(String email, String passwd) throws CEmailLoginFailedException{
        Members findMember = memberRepo.findByEmail(email).orElseThrow(CEmailLoginFailedException::new);

        if (!passwordEncoder.matches(passwd, findMember.getPasswd()))
            throw new CEmailLoginFailedException();

        String accessToken = jwtTokenService.createToken(findMember);
        LoginUserDetailDto loginUserDetailDto = LoginUserDetailDto.builder()
                .email(findMember.getEmail())
                .name(findMember.getName())
                .tokenType("")
                .accessToken(accessToken).build();

        return loginUserDetailDto;
    }
}
