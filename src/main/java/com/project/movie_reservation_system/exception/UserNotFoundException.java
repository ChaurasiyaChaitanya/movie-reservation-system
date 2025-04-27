package com.project.movie_reservation_system.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String message, HttpStatus httpStatus) {
        super(message,httpStatus);
    }
}
