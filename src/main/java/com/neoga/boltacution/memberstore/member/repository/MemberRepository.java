package com.neoga.boltacution.memberstore.member.repository;

import com.neoga.boltacution.memberstore.member.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByEmail(String email);
    Optional<Members> findByEmailAndProvider(String email, String provider);
}
