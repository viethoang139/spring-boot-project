package com.lvh.spring_boot_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lvh.spring_boot_project.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto, Long postId);

    CommentDto getCommentById(Long postId, Long commentId);

    List<CommentDto> getAllComments(Long postId) throws JsonProcessingException;

    CommentDto updateCommentById(CommentDto commentDto,
                                 Long postId,
                                 Long commentId);

    void deleteCommentById(Long postId, Long commentId);
}
