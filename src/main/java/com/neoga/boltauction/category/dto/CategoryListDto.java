package com.neoga.boltauction.category.dto;

import lombok.Data;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Data
public class CategoryListDto {
    private List<EntityModel> supCategoryList;
}
