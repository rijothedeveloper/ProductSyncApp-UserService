package com.george.productSyncApp.services;

import com.george.productSyncApp.dto.*;
import com.george.productSyncApp.entities.User;

public interface AuthenticationService {
    SignupResponse signup(SignupRequest signupRequest);
    JwtAuthenticationResponse signin(SigninRequest signinRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    EmailVerificationResponse verifyRegisterationEmail(String token);

}
