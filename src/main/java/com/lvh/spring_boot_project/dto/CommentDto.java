package com.lvh.spring_boot_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    @NotEmpty(message = "name should not be empty")
    @NotBlank(message = "name should not be blank")
    private String name;
    @NotEmpty(message = "email should not be empty")
    @NotBlank(message = "email should not be blank")
    @Email
    private String email;
    @NotEmpty(message = "body should not be empty")
    @NotBlank(message = "body should not be blank")
    private String body;
}
