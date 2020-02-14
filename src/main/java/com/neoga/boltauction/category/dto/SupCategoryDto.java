package com.neoga.boltauction.category.dto;

import lombok.Data;
import org.springframework.hateoas.Resource;

import java.util.List;

@Data
public class SupCategoryDto {
    private Long id;
    private String name;
    private List<Resource> subCategoryList;
}