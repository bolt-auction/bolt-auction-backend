package com.neoga.platform.memberstore.member.repository;

import com.neoga.platform.memberstore.member.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByUid(String uid);

    Optional<Members> findByUidAndProvider(String uid, String provider);
}
