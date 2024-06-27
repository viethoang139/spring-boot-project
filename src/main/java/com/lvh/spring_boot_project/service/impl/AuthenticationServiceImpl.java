package com.lvh.spring_boot_project.service.impl;

import com.lvh.spring_boot_project.dto.RegistrationRequest;
import com.lvh.spring_boot_project.entity.Role;
import com.lvh.spring_boot_project.entity.User;
import com.lvh.spring_boot_project.exception.ResourceNotFoundException;
import com.lvh.spring_boot_project.repository.RoleRepository;
import com.lvh.spring_boot_project.repository.UserRepository;
import com.lvh.spring_boot_project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
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
}
