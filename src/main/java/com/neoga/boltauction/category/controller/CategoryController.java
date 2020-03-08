package com.neoga.boltauction.category.controller;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.dto.CategoryDto;
import com.neoga.boltauction.category.dto.SupCategoryDto;
import com.neoga.boltauction.category.service.CategoryService;
import com.neoga.boltauction.category.dto.CategoryListDto;
import com.neoga.boltauction.item.controller.ItemController;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity getCategory() {

        List<Category> categoryList = categoryService.getSupCategoryList();
        Resources resource = new Resources(categoryList);

        return ResponseEntity.ok(resource);
    }
}
