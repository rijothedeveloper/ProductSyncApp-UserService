package com.george.userService.services;

import com.george.userService.dto.*;

public interface AuthenticationService {
    SignupResponse signup(SignupRequest signupRequest);
    JwtAuthenticationResponse signin(SigninRequest signinRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    EmailVerificationResponse verifyRegisterationEmail(String token);

}
