package com.neoga.boltauction.category.dto;

import lombok.Data;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Data
public class SupCategoryDto {
    private Long id;
    private String name;
    private List<EntityModel> subCategoryList;
}