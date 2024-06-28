package com.lvh.spring_boot_project.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvh.spring_boot_project.dto.AuthenticationRequest;
import com.lvh.spring_boot_project.dto.AuthenticationResponse;
import com.lvh.spring_boot_project.dto.RegistrationRequest;
import com.lvh.spring_boot_project.entity.Role;
import com.lvh.spring_boot_project.entity.User;
import com.lvh.spring_boot_project.repository.RoleRepository;
import com.lvh.spring_boot_project.repository.UserRepository;
import com.lvh.spring_boot_project.security.JwtService;
import com.lvh.spring_boot_project.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Override
    public void register(RegistrationRequest request) {
        Role role = roleRepository.findByName("ROLE_USER");
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(role))
                .build();
        userRepository.save(user);
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));
        var claims = new HashMap<String,Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullName",user.getFullName());
        var jwtToken = jwtService.generateToken(claims,user);
        var refreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder().accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if(userEmail != null){
            UserDetails userDetails = userRepository.findByEmail(userEmail).orElseThrow();
            if(jwtService.isTokenValid(refreshToken,userDetails)){
                var accessToken = jwtService.generateToken(userDetails);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


}
