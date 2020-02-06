package com.neoga.boltauction.image.repository;

import com.neoga.boltauction.image.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
}
