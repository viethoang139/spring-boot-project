package com.lvh.spring_boot_project.mapper;

import com.lvh.spring_boot_project.dto.CategoryDto;
import com.lvh.spring_boot_project.entity.Category;

public class CategoryMapper {
    public static CategoryDto mapToCategoryDto(Category category){
        CategoryDto categoryDto = CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription()).build();
        return categoryDto;
    }

    public static Category mapToCategory(CategoryDto categoryDto){
        Category category = Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .description(categoryDto.getDescription()).build();
        return category;
    }
}
