package com.neoga.boltauction.memberstore.member.service;

import com.neoga.boltauction.exception.custom.CExistUidSignUpException;
import com.neoga.boltauction.exception.custom.CAlreadySignUpException;
import com.neoga.boltauction.memberstore.member.domain.Members;
import com.neoga.boltauction.memberstore.member.dto.SignupRequestDto;
import com.neoga.boltauction.memberstore.member.repository.MemberRepository;
import com.neoga.boltauction.memberstore.store.domain.Store;
import com.neoga.boltauction.memberstore.store.repository.StoreRepository;
import com.neoga.boltauction.security.dto.KakaoProfile;
import com.neoga.boltauction.security.service.KakaoService;
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
    private final StoreRepository storeRepository;

    public Members findMemberById(Long member_id){
        return memberRepository.findById(member_id).get();
    }

    public Members saveMember(SignupRequestDto signupRequest) throws CExistUidSignUpException {
        //이메일 중복확인
        Optional<Members> existsMember = memberRepository.findByUid(signupRequest.getUid());
        if(existsMember.isPresent()){
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

        // store 생성
        Store store = new Store();
        store.changeMembers(newMember);
        storeRepository.save(store);

        return newMember;
    }

    public Members saveSocialMember(String provider, String accessToken, String name) throws CAlreadySignUpException {
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        Optional<Members> member = memberRepository.findByUidAndProvider(String.valueOf(profile.getId()), provider);
        if(member.isPresent())
            throw new CAlreadySignUpException();

        // store 생성
        Store store = new Store();
        Store saveStore = storeRepository.save(store);

        Members newMember = memberRepository.save(Members.builder()
                .uid(String.valueOf(profile.getId()))
                .provider(provider)
                .name(name)
                .role(Collections.singletonList("USER"))
                .store(saveStore)
                .build());

        return newMember;
    }
}
