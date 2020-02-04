package com.neoga.autionnara.category.service;

import com.neoga.autionnara.category.domain.Category;
import com.neoga.autionnara.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public void insertCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(Category category) {
        Category findCategory = categoryRepository.findById(category.getId()).get();

        findCategory.setName(category.getName());
        categoryRepository.save(findCategory);
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.deleteById(category.getId());
    }
}
