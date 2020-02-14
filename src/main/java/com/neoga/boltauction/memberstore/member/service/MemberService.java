package com.neoga.boltauction.memberstore.member.service;

import com.neoga.boltauction.exception.custom.CExistEmailSignUpException;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.dto.SignupRequestDto;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
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

    public Members findMemberById(Long member_id){
        return memberRepository.findById(member_id).get();
    }

    public Members saveMember(SignupRequestDto signupRequest) throws CExistEmailSignUpException {
        //이메일 중복확인
        Optional<Members> existsMember = memberRepository.findByUid(signupRequest.getUid());
        if(existsMember.isPresent()){
            throw new CExistEmailSignUpException();
        }

        Members newMember = Members.builder()
                .uid(signupRequest.getUid())
                .passwd(passwordEncoder.encode(signupRequest.getPasswd()))
                .name(signupRequest.getName())
                .role(Collections.singletonList("USER"))
                .build();

        memberRepository.save(newMember);
        return newMember;
    }
}
