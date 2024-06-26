package com.lvh.spring_boot_project.service;

import com.lvh.spring_boot_project.dto.PostDto;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);

    PostDto getPostById(Long postId);

    List<PostDto> getAllPosts();

    PostDto updatePostById(PostDto postDto, Long postId);

    void deletePostById(Long postId);

}
