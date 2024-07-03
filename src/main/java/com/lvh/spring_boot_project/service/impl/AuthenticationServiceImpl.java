package com.lvh.spring_boot_project.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvh.spring_boot_project.dto.AuthenticationRequest;
import com.lvh.spring_boot_project.dto.AuthenticationResponse;
import com.lvh.spring_boot_project.dto.EmailTemplateName;
import com.lvh.spring_boot_project.dto.RegistrationRequest;
import com.lvh.spring_boot_project.entity.Code;
import com.lvh.spring_boot_project.dto.Mail;
import com.lvh.spring_boot_project.entity.Role;
import com.lvh.spring_boot_project.entity.User;
import com.lvh.spring_boot_project.repository.*;
import com.lvh.spring_boot_project.security.JwtService;
import com.lvh.spring_boot_project.service.AuthenticationService;
import com.lvh.spring_boot_project.token.Token;
import com.lvh.spring_boot_project.token.TokenType;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
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
    private final TokenRepository tokenRepository;
    private final CodeRepository codeRepository;
    private final EmailService emailService;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic topic;
    @Override
    public void register(RegistrationRequest request) throws MessagingException {
        Role role = roleRepository.findByName("ROLE_USER");
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(role))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newCode = generateAndSaveActivationCode(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                newCode,
                "Account activation"
        );
        Mail mail = Mail.builder()
                .email(user.getEmail())
                .username(user.getFullName())
                .emailTemplateName(String.valueOf(EmailTemplateName.ACTIVATE_ACCOUNT))
                .subject("Account activation").build();
        redisTemplate.convertAndSend(topic.getTopic(),mail.toString());
    }

    private String generateAndSaveActivationCode(User user) {
        String generatedCode = generateActivationCode(6);
        var code = Code.builder()
                .code(generatedCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        codeRepository.save(code);
        return generatedCode;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder builder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            builder.append(characters.charAt(randomIndex));
        }
        return builder.toString();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false).build();
        tokenRepository.save(token);
    }

    private void revokeAllUserToken(User user){
        var validUserTokens = tokenRepository.findAllByValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
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
        revokeAllUserToken(user);
        saveUserToken(user,jwtToken);
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
            var user = userRepository.findByEmail(userEmail).orElseThrow();
            if(jwtService.isTokenValid(refreshToken,user)){
                var accessToken = jwtService.generateToken(user);
                revokeAllUserToken(user);
                saveUserToken(user,accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Transactional
    @Override
    public void activateAccount(String code) throws MessagingException {
        Code savedCode = codeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if(LocalDateTime.now().isAfter(savedCode.getExpiresAt())){
            sendValidationEmail(savedCode.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email address");
        }
        var user = userRepository.findById(savedCode.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("Can not found username"));

        user.setEnabled(true);
        userRepository.save(user);
        savedCode.setValidatedAt(LocalDateTime.now());
        codeRepository.save(savedCode);
    }


}
