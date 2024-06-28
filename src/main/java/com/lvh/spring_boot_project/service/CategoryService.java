package com.lvh.spring_boot_project.service;

import com.lvh.spring_boot_project.dto.CategoryDto;
import com.lvh.spring_boot_project.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryDto categoryDto);

    CategoryDto getCategoryById(Long categoryId);

    List<CategoryDto> getAllCategories();

    CategoryResponseDto updateCategoryById(CategoryDto categoryDto,
                                   Long categoryId);

    void deleteCategoryById(Long categoryId);

}
