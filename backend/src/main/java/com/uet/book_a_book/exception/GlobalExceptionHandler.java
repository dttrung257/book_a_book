package com.uet.book_a_book.exception;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorDetails> handleGlobalException(Exception e, WebRequest webRequest) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(),
				List.of(webRequest.getDescription(false)));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Description", "Error").body(errorDetails);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	ResponseEntity<Object> notFoundEmailExceptionHandler(UsernameNotFoundException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), "Not found account",
				List.of(e.getMessage()));
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
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
	
	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<Object> illegalStateExceptionHandler(IllegalStateException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Illegal State",
				List.of(e.getMessage()));
		return ResponseEntity.badRequest().body(errorDetails);
	}
	
	@ExceptionHandler(StatusNotFoundException.class)
	ResponseEntity<Object> statusNotFoundExceptionHandler(StatusNotFoundException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), "Not Found Status",
				List.of(e.getMessage()));
		return ResponseEntity.badRequest().body(errorDetails);
	}
	
	@ExceptionHandler(ValidationException.class)
	ResponseEntity<Object> validationExceptionHandler(ValidationException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Validation Error", List.of(e.getMessage()));
		return ResponseEntity.badRequest().body(errorDetails);
	}
}
