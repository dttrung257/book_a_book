package com.uet.book_a_book.exception;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UsernameNotFoundException.class)
	ResponseEntity<Object> notFoundEmailExceptionHandler(UsernameNotFoundException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Not found account",
				List.of("Email is not existed"));
		return ResponseEntity.badRequest().body(errorDetails);
	}

	@ExceptionHandler(BadCredentialsException.class)
	ResponseEntity<Object> badCredentialsExceptionHandler(BadCredentialsException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Bad Credentials",
				List.of(e.getMessage()));
		return ResponseEntity.badRequest().body(errorDetails);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<Object> validationExceptionHandler(MethodArgumentNotValidException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Validation Error",
				e.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
						.collect(Collectors.toList()));
		return ResponseEntity.badRequest().body(errorDetails);
	}
}
