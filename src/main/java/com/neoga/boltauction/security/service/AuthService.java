package com.neoga.boltauction.security.service;

import com.neoga.boltauction.exception.custom.CEmailLoginFailedException;
import com.neoga.boltauction.exception.custom.CMemberNotFoundException;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import com.neoga.boltauction.security.dto.KakaoProfile;
import com.neoga.boltauction.security.dto.LoginDto;
import com.neoga.boltauction.security.dto.LoginInfo;
import com.neoga.boltauction.security.dto.LoginUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService{

    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;

    //로그인 정보를 받아 검증 후 jwt 토큰 생성 후 로그인 정보 반환
    public LoginUserDto login(LoginDto loginDto) throws CEmailLoginFailedException{
        String email = loginDto.getEmail();
        String passwd = loginDto.getPasswd();
        Members findMember = memberRepository.findByEmail(email)
                .orElseThrow(CEmailLoginFailedException::new);

        if (!passwordEncoder.matches(passwd, findMember.getPasswd()))
            throw new CEmailLoginFailedException();

        String accessToken = jwtTokenService.createToken(findMember);

        return loginUserDtoBuilder(accessToken, findMember);
    }

    //소셜 accessToken이용하여 jwt 토큰 생성 후 로그인 정보 반환
    public LoginUserDto socialLogin(String socialAccessToken, String provider) throws CMemberNotFoundException{
        KakaoProfile profile = kakaoService.getKakaoProfile(socialAccessToken);
        Members findMember = memberRepository.findByEmailAndProvider(String.valueOf(profile.getId()), provider)
                .orElseThrow(()->new CMemberNotFoundException("member not found : you need signup"));

        String accessToken = jwtTokenService.createToken(findMember);

        return loginUserDtoBuilder(accessToken, findMember);
    }

    // 저장된 인증정보에서 현재 로그인 사용자정보 조회
    public LoginInfo getLoginInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> detail = ((Map)authentication.getDetails());
        LoginInfo logininfo = LoginInfo.builder()
                .member_id(Long.valueOf((String)detail.get("id")))
                .email((String)detail.get("email"))
                .name((String)detail.get("name"))
                .role((List)detail.get("authorities")).build();
        return logininfo;
    }

    public LoginUserDto loginUserDtoBuilder(String accessToken, Members findMember){
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .member_id(findMember.getId())
                .email(findMember.getEmail())
                .name(findMember.getName())
                .tokenType("Bearer")
                .accessToken(accessToken).build();
        return loginUserDto;
    }
}
