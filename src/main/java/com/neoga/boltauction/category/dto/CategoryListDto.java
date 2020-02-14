package com.neoga.boltauction.category.dto;

import lombok.Data;
import org.springframework.hateoas.Resource;

import java.util.List;

@Data
public class CategoryListDto {
    private List<Resource> supCategoryList;
}
