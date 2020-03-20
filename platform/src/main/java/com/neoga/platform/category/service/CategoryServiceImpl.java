package com.neoga.platform.category.service;

import com.neoga.platform.category.domain.Category;
import com.neoga.platform.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(cacheNames = "categoryList")
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

}
