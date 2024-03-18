package com.george.productSyncApp.dto;

import com.george.productSyncApp.entities.User;

public record SignupResponse(User user, String error) {
}
