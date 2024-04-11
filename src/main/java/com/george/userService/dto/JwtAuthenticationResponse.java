package com.george.userService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class JwtAuthenticationResponse {
    private String token;
    private String refreshToken;
    private Map<String, String> error;
}
