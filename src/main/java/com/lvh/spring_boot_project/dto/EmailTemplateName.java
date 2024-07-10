package com.lvh.spring_boot_project.dto;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate_account"),
    NOTIFICATION_EMAIL("notification_email");
    private final String name;

    EmailTemplateName(String name){
        this.name = name;
    }
}
