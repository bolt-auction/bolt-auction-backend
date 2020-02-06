package com.neoga.boltauction.image.repository;

import com.neoga.boltauction.image.domain.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
}
