package com.lvh.spring_boot_project.service;

import com.lvh.spring_boot_project.dto.PostDto;
import com.lvh.spring_boot_project.dto.PostResponseDto;

import java.util.List;

public interface PostService {

    PostResponseDto createPost(PostDto postDto);

    PostDto getPostById(Long postId);

    List<PostDto> getAllPosts();

    PostResponseDto updatePostById(PostDto postDto, Long postId);

    void deletePostById(Long postId);

}
