package com.neoga.autionnara.category.controller;

import com.neoga.autionnara.category.domain.Category;
import com.neoga.autionnara.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategory() {
        return categoryService.getCategoryList();
    }
}
