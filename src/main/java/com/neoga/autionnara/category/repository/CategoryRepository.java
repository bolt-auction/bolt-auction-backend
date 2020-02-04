package com.neoga.autionnara.category.repository;

import com.neoga.autionnara.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
