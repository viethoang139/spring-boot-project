package com.lvh.spring_boot_project.controller;

import com.lvh.spring_boot_project.dto.AuthenticationRequest;
import com.lvh.spring_boot_project.dto.AuthenticationResponse;
import com.lvh.spring_boot_project.dto.RegistrationRequest;
import com.lvh.spring_boot_project.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.ok("Register successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request){
       return ResponseEntity.ok(authenticationService.login(request));
    }
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request,response);
    }

    @GetMapping("/activate-account")
    public void confirm(@RequestParam String code) throws MessagingException{
        authenticationService.activateAccount(code);
    }
}
