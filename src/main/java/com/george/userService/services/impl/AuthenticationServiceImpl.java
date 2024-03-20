package com.george.userService.services.impl;

import com.george.userService.dto.*;
import com.george.userService.dto.external.RegisterationEmailRequest;
import com.george.userService.entities.EmailVerificationToken;
import com.george.userService.entities.Role;
import com.george.userService.entities.User;
import com.george.userService.repository.EmailVerificationTokenRepository;
import com.george.userService.repository.UserRepository;
import com.george.userService.services.AuthenticationService;
import com.george.userService.services.JwtService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

@Service
@Data
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
//    private final WebClient webClient;


    @Override
    public SignupResponse signup(SignupRequest signupRequest) {
        if(userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            return new SignupResponse(null, "email not available");
        }
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setFirstname(signupRequest.getFirstName());
        user.setLastname(signupRequest.getLastName());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setActive(false);
        user.setEmailVerified(false);
        var savedUser = userRepository.save(user);
        sendVerificationEmail(user);
        return new SignupResponse(savedUser, null);
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest signinRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
        var user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("invalid email"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow();
        if(!jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            return null;
        }
        var jwt = jwtService.generateToken(user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
        return jwtAuthenticationResponse;
    }

    @Override
    public EmailVerificationResponse verifyRegisterationEmail(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("invalid email verification token"));
        User user = emailVerificationToken.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);
        emailVerificationTokenRepository.delete(emailVerificationToken);
        return new EmailVerificationResponse("verified");
    }

    private EmailVerificationToken generateEmailVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(token);
        emailVerificationToken.setCreatedAt(new Date(System.currentTimeMillis()));
        emailVerificationToken.setUser(user);
        return emailVerificationToken;
    }

    private void sendVerificationEmail(User user) {
        EmailVerificationToken emailVerificationToken = generateEmailVerificationToken(user);
        emailVerificationTokenRepository.save(emailVerificationToken);
        RegisterationEmailRequest registerationEmailRequest = new RegisterationEmailRequest(user.getEmail(), emailVerificationToken.getToken());
        // ipc to email service
        WebClient webClient = WebClient.create();
        Mono<String> emailMono = webClient.post()
                .uri("http://localhost:8082/sendMail")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(registerationEmailRequest)
                .retrieve()
                .bodyToMono(String.class);
        emailMono.subscribe(System.out::println);

    }


}
