package com.lvh.spring_boot_project.service.impl;

import com.lvh.spring_boot_project.dto.CommentDto;
import com.lvh.spring_boot_project.dto.PostDto;
import com.lvh.spring_boot_project.entity.Comment;
import com.lvh.spring_boot_project.entity.Post;
import com.lvh.spring_boot_project.exception.ResourceNotFoundException;
import com.lvh.spring_boot_project.mapper.CommentMapper;
import com.lvh.spring_boot_project.mapper.PostMapper;
import com.lvh.spring_boot_project.repository.CommentRepository;
import com.lvh.spring_boot_project.repository.PostRepository;
import com.lvh.spring_boot_project.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = PostMapper.mapToPost(postDto);
        Post savedPost = postRepository.save(post);
        return PostMapper.mapToPostDto(savedPost);
    }
    @Override
    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","ID",postId.toString()));
        PostDto postDto = PostMapper.mapToPostDto(post);
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> commentDtoList = comments.stream()
                .map(comment -> CommentMapper.mapToCommentDto(comment)).collect(Collectors.toList());
        postDto.setComments(commentDtoList);
        return postDto;
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDto> postDtos = new ArrayList<>();
        for(Post post : posts){
            PostDto postDto = PostMapper.mapToPostDto(post);
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            List<CommentDto> commentDtoList = comments.stream()
                    .map(comment -> CommentMapper.mapToCommentDto(comment)).collect(Collectors.toList());
            postDto.setComments(commentDtoList);
            postDtos.add(postDto);
        }
        return postDtos;
    }

    @Override
    public PostDto updatePostById(PostDto postDto, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","ID",postId.toString()));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post updatedPost = postRepository.save(post);
        return PostMapper.mapToPostDto(updatedPost);
    }

    @Override
    public void deletePostById(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","ID",postId.toString()));
        postRepository.deleteById(postId);
    }
}
