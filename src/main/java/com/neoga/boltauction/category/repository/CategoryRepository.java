package com.neoga.boltauction.category.repository;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.dto.CategoryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllBySupCategoryIsNull();
    List<Category> findAllBySupCategoryId(Long id);
}
