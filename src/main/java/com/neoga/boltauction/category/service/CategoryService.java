package com.neoga.boltauction.category.service;

import com.neoga.boltauction.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getCategoryList();
    Optional<Category> getCategory(Long categoryId);
}
