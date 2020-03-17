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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
        CategoryListDto categoryList = new CategoryListDto();

        //get all category List
        List<Category> findCategoryList = categoryService.getCategoryList();


        //get all super category List
        List<Category> supCategoryList = new ArrayList<>();
        for (Category category : findCategoryList) {
            if (category.getSupCategory() == null) {
                supCategoryList.add(category);
            }
        }
        //map super category list to super category dto list
        List<SupCategoryDto> supCategoryDtoList = supCategoryList.stream()
                .map(category -> modelMapper.map(category, SupCategoryDto.class))
                .collect(Collectors.toList());
        //set sub category list
        supCategoryDtoList.forEach(supCategoryDto -> {
            //get sub category list
            List<Category> subCategoryList = new ArrayList<>();
            for (Category category : findCategoryList) {
                if (category.getSupCategory() != null &&
                        category.getSupCategory().getId().equals(supCategoryDto.getId())) {
                    subCategoryList.add(category);
                }
            }
            //map sub category to category dto
            List<CategoryDto> subCategoryDtoList = subCategoryList.stream()
                    .map(category -> modelMapper.map(category, CategoryDto.class))
                    .collect(Collectors.toList());
            //make sub category entity model list
            List<Resource> subCategoryEntityModelList = subCategoryDtoList
                    .stream().map(categoryDto -> {
                        Resource resource = new Resource(categoryDto);
                        resource.add(linkTo(methodOn(ItemController.class).getItems(categoryDto.getId(), null, null)).withRel("item-list"));
                        return resource;
                    }).collect(Collectors.toList());
            //set sub category entity model list to sup category
            supCategoryDto.setSubCategoryList(subCategoryEntityModelList);
        });
        // make sup category entity model list
        List<Resource> supCategoryEntityModelList = supCategoryDtoList.stream().map(supCategoryDto -> {
            Resource resource = new Resource(supCategoryDto);
            resource.add(linkTo(methodOn(ItemController.class).getItems(supCategoryDto.getId(), null, null)).withRel("item-list"));
            return resource;
        }).collect(Collectors.toList());

        // set category list dto
        categoryList.setSupCategoryList(supCategoryEntityModelList);
        // make category list dto entity model
        Resource resource = new Resource(categoryList);
        resource.add(new Link("/swagger-ui.html#/category-controller/getCategoryUsingGET").withRel("profile"));

        return ResponseEntity.ok(resource);
    }
}
