package com.lvh.spring_boot_project.mapper;

import com.lvh.spring_boot_project.dto.PostDto;
import com.lvh.spring_boot_project.dto.PostResponseDto;
import com.lvh.spring_boot_project.entity.Post;

public class PostResponseMapper {
    public static PostResponseDto mapToPostResponse(PostDto postDto){
        return PostResponseDto.builder().title(postDto.getTitle())
                .description(postDto.getDescription())
                .content(postDto.getContent())
                .categoryId(postDto.getCategoryId()).build();
    }

    public static PostResponseDto mapToPostResponse(Post post){
        return PostResponseDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .categoryId(post.getCategory().getId()).build();
    }

}
