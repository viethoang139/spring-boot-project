package com.lvh.spring_boot_project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryResponseDto {
    private String name;
    private String description;
}
