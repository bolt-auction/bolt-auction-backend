package com.neoga.autionnara.category.service;

import com.neoga.autionnara.category.domain.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategoryList();

    Category getCategory(Long id);

    void insertCategory(Category category);

    void updateCategory(Category category);

    void deleteCategory(Category category);
}
