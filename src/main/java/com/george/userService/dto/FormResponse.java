package com.george.userService.dto;

import java.util.Map;

public record FormResponse<T>(boolean success, T data, Map<String, String> error) {
}
