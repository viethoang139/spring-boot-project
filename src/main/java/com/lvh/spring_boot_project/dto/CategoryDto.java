package com.lvh.spring_boot_project.dto;

import com.lvh.spring_boot_project.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryDto {
    private Long id;
    @NotEmpty(message = "name is mandatory")
    @NotBlank(message = "name should not be blank")
    private String name;
    @NotEmpty(message = "description is mandatory")
    @NotBlank(message = "description should not be blank")
    private String description;
    private List<PostResponseDto> posts;
}
