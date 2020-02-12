package com.neoga.boltauction.memberstore.store.repository;

import com.neoga.boltauction.memberstore.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
