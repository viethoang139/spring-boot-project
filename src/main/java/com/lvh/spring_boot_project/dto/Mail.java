package com.lvh.spring_boot_project.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Mail {
    private String email;
    private String username;
    private String emailTemplateName;
    private String subject;
}
