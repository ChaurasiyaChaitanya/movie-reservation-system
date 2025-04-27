package com.project.movie_reservation_system.exception;

import org.springframework.http.HttpStatus;

public class SeatAlreadyLockedException extends CustomException {
	  public SeatAlreadyLockedException(String message, HttpStatus httpStatus) {
	    super(message,httpStatus);
	  }
	}
