package com.neoga.platform.category.dto;

import lombok.Data;
import org.springframework.hateoas.Resource;

import java.util.List;

@Data
public class CategoryListDto {
    private List<Resource> supCategoryList;
}
