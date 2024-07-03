package com.lvh.spring_boot_project.service;

import com.lvh.spring_boot_project.dto.AuthenticationRequest;
import com.lvh.spring_boot_project.dto.AuthenticationResponse;
import com.lvh.spring_boot_project.dto.RegistrationRequest;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    void register(RegistrationRequest request) throws MessagingException;
    AuthenticationResponse login(AuthenticationRequest authenticationRequest);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void activateAccount(String code) throws MessagingException;
}
