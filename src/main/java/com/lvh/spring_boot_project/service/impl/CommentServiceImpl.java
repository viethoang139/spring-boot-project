package com.lvh.spring_boot_project.service.impl;

import com.lvh.spring_boot_project.dto.CommentDto;
import com.lvh.spring_boot_project.entity.Comment;
import com.lvh.spring_boot_project.entity.Post;
import com.lvh.spring_boot_project.exception.BlogRestApiException;
import com.lvh.spring_boot_project.exception.ResourceNotFoundException;
import com.lvh.spring_boot_project.mapper.CommentMapper;
import com.lvh.spring_boot_project.repository.CommentRepository;
import com.lvh.spring_boot_project.repository.PostRepository;
import com.lvh.spring_boot_project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Override
    public CommentDto createComment(CommentDto commentDto, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","ID",postId.toString()));
        Comment comment = CommentMapper.mapToComment(commentDto);
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.mapToCommentDto(savedComment);
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","ID",postId.toString()));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment","ID",commentId.toString()));

        if(!postId.equals(comment.getPost().getId())){
            throw new BlogRestApiException("Comment does not belong to this post");
        }

        return CommentMapper.mapToCommentDto(comment);

    }

    @Override
    public List<CommentDto> getAllComments(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","ID",postId.toString()));
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(comment -> CommentMapper.mapToCommentDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto updateCommentById(CommentDto commentDto, Long postId, Long commentId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","ID",postId.toString()));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment","ID",commentId.toString()));

        if(!postId.equals(comment.getPost().getId())){
            throw new BlogRestApiException("Comment does not belong to this post");
        }
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return CommentMapper.mapToCommentDto(updatedComment);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","ID",postId.toString()));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment","ID",commentId.toString()));

        if(!postId.equals(comment.getPost().getId())){
            throw new BlogRestApiException("Comment does not belong to this post");
        }
        commentRepository.deleteById(commentId);
    }
}
