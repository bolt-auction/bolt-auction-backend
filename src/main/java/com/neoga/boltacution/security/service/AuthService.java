package com.neoga.boltacution.security.service;

import com.neoga.boltacution.exception.custom.CEmailLoginFailedException;
import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import com.neoga.boltacution.security.dto.LoginDto;
import com.neoga.boltacution.security.dto.LoginUserDto;
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
    public LoginUserDto login(LoginDto loginDto) throws CEmailLoginFailedException{
        String email = loginDto.getEmail();
        String passwd = loginDto.getPasswd();
        Members findMember = memberRepo.findByEmail(email).orElseThrow(CEmailLoginFailedException::new);

        if (!passwordEncoder.matches(passwd, findMember.getPasswd()))
            throw new CEmailLoginFailedException();

        String accessToken = jwtTokenService.createToken(findMember);
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .member_id(findMember.getId())
                .email(findMember.getEmail())
                .name(findMember.getName())
                .tokenType("Bearer")
                .accessToken(accessToken).build();

        return loginUserDto;
    }
}
