package com.project.movie_reservation_system.exception;

import org.springframework.http.HttpStatus;

public class ReservationNotCancellableException extends CustomException {
    public ReservationNotCancellableException(String message, HttpStatus httpStatus) {
        super(message,httpStatus);
    }
}
