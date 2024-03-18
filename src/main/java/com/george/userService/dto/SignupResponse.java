package com.george.userService.dto;

import com.george.userService.entities.User;

public record SignupResponse(User user, String error) {
}
