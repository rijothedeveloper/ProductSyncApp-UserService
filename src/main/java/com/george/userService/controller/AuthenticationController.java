package com.george.userService.controller;

import com.george.userService.dto.*;
import com.george.userService.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/status")
    @CrossOrigin
    public ResponseEntity<FormResponse<String>> getStatus(){
        return ResponseEntity.ok(new FormResponse<String>(true, "working fine", new HashMap<>()));
    }
    @GetMapping("/verify_user_email")
    public ResponseEntity<EmailVerificationResponse> veifyUserEmail(@RequestParam String token){
        return ResponseEntity.ok(authenticationService.verifyRegisterationEmail(token));
    }

    @PostMapping("/signup")
    @CrossOrigin

    public ResponseEntity<FormResponse<String>> signup(@Valid @RequestBody SignupRequest signupRequest) {

//        return ResponseEntity.ok(authenticationService.signup(signupRequest));
        FormResponse<String> response = authenticationService.signup(signupRequest);
//        return new ResponseEntity<String>(authenticationService.signup(signupRequest), HttpStatus.CREATED);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @CrossOrigin
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@Valid @RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FormResponse<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new FormResponse<>(false,null, errors);
    }

}
