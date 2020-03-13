package com.neoga.boltauction.category.service;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.dto.CategoryListDto;
import com.neoga.boltauction.category.dto.SupCategoryDto;
import com.neoga.boltauction.exception.custom.CCategoryNotFoundException;
import com.neoga.boltauction.category.repository.CategoryRepository;
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
