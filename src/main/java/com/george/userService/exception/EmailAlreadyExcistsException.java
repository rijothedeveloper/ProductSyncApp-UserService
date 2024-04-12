package com.george.userService.exception;


public class EmailAlreadyExcistsException extends RuntimeException {
    public EmailAlreadyExcistsException(String message) {
        super(message);
    }

}
