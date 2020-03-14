package com.neoga.boltauction.item.repository;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.item.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByNameIsContaining(Pageable pageable, String string);
    @Query(value = "select i from Item i " +
            "where i.category.id=:categoryId or i.category.supCategory.id=:categoryId",
            countQuery = "select count(i) from Item i"
    )
    Page<Item> findAllByCategory_IdEquals(Pageable pageable, Long categoryId);
    List<Item> findAllByMembers_Id(Long memberId);
}
