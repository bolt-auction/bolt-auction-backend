package com.neoga.boltacution.memberstore.member.service;

import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Members findMemberById(Long member_id){
        return memberRepository.findById(member_id).get();
    }
}
