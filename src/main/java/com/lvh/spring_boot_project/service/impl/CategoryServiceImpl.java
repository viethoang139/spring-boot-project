package com.lvh.spring_boot_project.service.impl;

import com.lvh.spring_boot_project.dto.CategoryDto;
import com.lvh.spring_boot_project.dto.CategoryResponseDto;
import com.lvh.spring_boot_project.dto.PostResponseDto;
import com.lvh.spring_boot_project.entity.Category;
import com.lvh.spring_boot_project.entity.Post;
import com.lvh.spring_boot_project.exception.ResourceNotFoundException;
import com.lvh.spring_boot_project.mapper.CategoryMapper;
import com.lvh.spring_boot_project.mapper.CategoryResponseMapper;
import com.lvh.spring_boot_project.mapper.PostResponseMapper;
import com.lvh.spring_boot_project.repository.CategoryRepository;
import com.lvh.spring_boot_project.repository.PostRepository;
import com.lvh.spring_boot_project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    @Override
    public CategoryResponseDto createCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.mapToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponseMapper.mapToCategoryResponse(CategoryMapper.mapToCategoryDto(savedCategory));
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","ID",categoryId.toString()));
        List<Post> posts = postRepository.findByCategoryId(categoryId);
        List<PostResponseDto> postDtos = posts.stream().map(PostResponseMapper::mapToPostResponse)
                .collect(Collectors.toList());
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
        categoryDto.setPosts(postDtos);
        return categoryDto;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for(Category category : categories){
            CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
            List<Post> posts = postRepository.findByCategoryId(category.getId());
            List<PostResponseDto> postDtos = posts.stream().map(PostResponseMapper::mapToPostResponse)
                    .collect(Collectors.toList());
            categoryDto.setPosts(postDtos);
            categoryDtos.add(categoryDto);
        }
        return categoryDtos;
    }

    @Override
    public CategoryResponseDto updateCategoryById(CategoryDto categoryDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","ID",categoryId.toString()));
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category updatedCategory = categoryRepository.save(category);
        CategoryDto categorydto = CategoryMapper.mapToCategoryDto(updatedCategory);
        return CategoryResponseMapper.mapToCategoryResponse(categorydto);
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","ID",categoryId.toString()));
        categoryRepository.deleteById(categoryId);
    }
}
