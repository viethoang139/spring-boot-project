package com.lvh.spring_boot_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lvh.spring_boot_project.dto.CommentDto;
import com.lvh.spring_boot_project.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody @Valid CommentDto commentDto,
                                                    @PathVariable Long postId){
        return new ResponseEntity<>(commentService.createComment(commentDto, postId), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long postId, @PathVariable Long commentId){
        return ResponseEntity.ok(commentService.getCommentById(postId, commentId));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getAllComments(@PathVariable Long postId) throws JsonProcessingException {
        return ResponseEntity.ok(commentService.getAllComments(postId));
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentById(@RequestBody @Valid CommentDto commentDto,
                                                        @PathVariable Long postId,
                                                        @PathVariable Long commentId){
        return ResponseEntity.ok(commentService.updateCommentById(commentDto, postId, commentId));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long postId,
                                                    @PathVariable Long commentId){
        commentService.deleteCommentById(postId, commentId);
        return ResponseEntity.ok("Delete comment successfully");
    }

}
