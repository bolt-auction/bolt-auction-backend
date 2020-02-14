package com.neoga.boltauction.category.controller;

import com.neoga.boltauction.category.domain.Category;
import com.neoga.boltauction.category.dto.CategoryDto;
import com.neoga.boltauction.category.dto.SupCategoryDto;
import com.neoga.boltauction.category.service.CategoryService;
import com.neoga.boltauction.category.dto.CategoryListDto;
import com.neoga.boltauction.item.controller.ItemController;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity getCategory() {

        CategoryListDto categoryListDto = new CategoryListDto();

        //get all super category List
        List<Category> supCategoryList = categoryService.getSupCategoryList();
        //map super category list to super category dto list
        List<SupCategoryDto> supCategoryDtoList = supCategoryList.stream()
                .map(category -> modelMapper.map(category, SupCategoryDto.class))
                .collect(Collectors.toList());
        //set sub category list
        supCategoryDtoList.forEach(supCategoryDto -> {
            //get sub category list
            List<Category> subCategoryList = categoryService
                    .getSubCategoryList(supCategoryDto.getId());
            //map sub category to category dto
            List<CategoryDto> subCategoryDtoList = subCategoryList.stream()
                    .map(category -> modelMapper.map(category, CategoryDto.class))
                    .collect(Collectors.toList());
            //make sub category entity model list
            List<EntityModel> subCategoryEntityModelList = subCategoryDtoList
                    .stream().map(categoryDto -> {
                        EntityModel entityModel = new EntityModel(categoryDto);
                        entityModel.add(linkTo(methodOn(ItemController.class).getItems(categoryDto.getId(), null, null)).withRel("item-list"));
                        return entityModel;
                    }).collect(Collectors.toList());
            //set sub category entity model list to sup category
            supCategoryDto.setSubCategoryList(subCategoryEntityModelList);
        });
        // make sup category entity model list
        List<EntityModel> supCategoryEntityModelList = supCategoryDtoList.stream().map(supCategoryDto -> {
            EntityModel entityModel = new EntityModel(supCategoryDto);
            entityModel.add(linkTo(methodOn(ItemController.class).getItems(supCategoryDto.getId(), null, null)).withRel("item-list"));
            return entityModel;
        }).collect(Collectors.toList());

        // set category list dto
        categoryListDto.setSupCategoryList(supCategoryEntityModelList);
        // make category list dto entity model
        EntityModel entityModel = new EntityModel(categoryListDto);
        entityModel.add(new Link("/").withRel("profile"));

        return ResponseEntity.ok(entityModel);
    }
}
