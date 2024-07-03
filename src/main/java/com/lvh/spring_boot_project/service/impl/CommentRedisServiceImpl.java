package com.lvh.spring_boot_project.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvh.spring_boot_project.dto.CommentDto;
import com.lvh.spring_boot_project.service.CommentRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentRedisServiceImpl implements CommentRedisService {
    private final RedisTemplate<String,Object> redisTemplate;
    private final ObjectMapper objectMapper;
    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public List<CommentDto> getAllComments(Long postId) throws JsonProcessingException {
        String key = getKeyFrom(postId);
        String json = (String) redisTemplate.opsForValue().get(key);
        List<CommentDto> commentDtos =
                json != null ? objectMapper.readValue(json, new TypeReference<List<CommentDto>>() {
                }) : null;
        return commentDtos;
    }

    @Override
    public void saveAllComments(List<CommentDto> commentDtos, Long postId) throws JsonProcessingException {
        String key = this.getKeyFrom(postId);
        String json = objectMapper.writeValueAsString(commentDtos);
        redisTemplate.opsForValue().set(key,json);
    }

    private String getKeyFrom(Long postId){
        return String.format("all_comments:%d",postId);
    }
}
