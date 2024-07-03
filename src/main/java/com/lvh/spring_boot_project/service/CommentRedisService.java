package com.lvh.spring_boot_project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lvh.spring_boot_project.dto.CommentDto;

import java.util.List;

public interface CommentRedisService {
    void clear();

    List<CommentDto> getAllComments(Long postId) throws JsonProcessingException;

    void saveAllComments(List<CommentDto> commentDtos, Long postId) throws JsonProcessingException;
}
