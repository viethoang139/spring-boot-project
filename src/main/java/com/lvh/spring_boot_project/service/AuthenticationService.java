package com.lvh.spring_boot_project.service;

import com.lvh.spring_boot_project.dto.AuthenticationRequest;
import com.lvh.spring_boot_project.dto.AuthenticationResponse;
import com.lvh.spring_boot_project.dto.RegistrationRequest;

public interface AuthenticationService {
    void register(RegistrationRequest request);

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

}
