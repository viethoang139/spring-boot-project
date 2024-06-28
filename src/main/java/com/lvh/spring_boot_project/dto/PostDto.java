package com.lvh.spring_boot_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    @NotEmpty(message = "title should not be empty")
    @NotBlank(message = "title should not be blank")
    private String title;
    @NotEmpty(message = "description should not be empty")
    @NotBlank(message = "description should not be blank")
    private String description;
    @NotEmpty(message = "content should not be empty")
    @NotBlank(message = "content should not be blank")
    private String content;
    private List<CommentDto> comments;
    private Long categoryId;
}
