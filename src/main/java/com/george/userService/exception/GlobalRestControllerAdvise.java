package com.george.userService.exception;

import com.george.userService.dto.FormResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalRestControllerAdvise extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyExcistsException.class)
    public FormResponse<String> resourceNotFoundException(EmailAlreadyExcistsException ex, WebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("email", "email already exists");
        return new FormResponse<>(false,null, errorMap);
    }
}
