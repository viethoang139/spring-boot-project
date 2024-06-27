package com.lvh.spring_boot_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BlogRestApiException extends RuntimeException{
    public BlogRestApiException(String message){
        super(message);
    }
}
