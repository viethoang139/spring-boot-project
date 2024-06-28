package com.lvh.spring_boot_project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostResponseDto {
    private String title;
    private String description;
    private String content;
    private Long categoryId;
}
