package com.neoga.platform.memberstore.member.service;

import com.neoga.platform.exception.custom.CExistUidSignUpException;
import com.neoga.platform.exception.custom.CAlreadySignUpException;
import com.neoga.platform.memberstore.member.domain.Members;
import com.neoga.platform.memberstore.member.dto.SignupRequestDto;
import com.neoga.platform.memberstore.member.repository.MemberRepository;
import com.neoga.platform.security.dto.KakaoProfile;
import com.neoga.platform.security.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;

    public Members findMemberById(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public Members saveMember(SignupRequestDto signupRequest) {
        //이메일 중복확인
        Optional<Members> existsMember = memberRepository.findByUid(signupRequest.getUid());
        if (existsMember.isPresent()) {
            throw new CExistUidSignUpException();
        }

        Members newMember = Members.builder()
                .uid(signupRequest.getUid())
                .provider("local")
                .passwd(passwordEncoder.encode(signupRequest.getPasswd()))
                .name(signupRequest.getName())
                .role(Collections.singletonList("USER"))
                .build();

        memberRepository.save(newMember);

        return newMember;
    }

    public Members saveSocialMember(String provider, String accessToken, String name) {
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        Optional<Members> member = memberRepository.findByUidAndProvider(String.valueOf(profile.getId()), provider);
        if (member.isPresent())
            throw new CAlreadySignUpException();

        return memberRepository.save(Members.builder()
                .uid(String.valueOf(profile.getId()))
                .provider(provider)
                .name(name)
                .role(Collections.singletonList("USER"))
                .build());
    }
}
