package com.lvh.spring_boot_project.mapper;

import com.lvh.spring_boot_project.dto.CategoryDto;
import com.lvh.spring_boot_project.dto.CategoryResponseDto;

public class CategoryResponseMapper {
    public static CategoryResponseDto mapToCategoryResponse(CategoryDto categoryDto){
        return CategoryResponseDto.builder().name(categoryDto.getName())
                .description(categoryDto.getDescription()).build();
    }
}
