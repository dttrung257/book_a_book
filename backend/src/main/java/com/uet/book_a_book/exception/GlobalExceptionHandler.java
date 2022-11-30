package com.uet.book_a_book.exception;

import java.util.Date;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.uet.book_a_book.exception.jwt.InvalidJwtTokenException;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleGlobalException(Exception e, WebRequest webRequest) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				e.getMessage(), webRequest.getDescription(false));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorDetails> handleIllegalStateException(IllegalStateException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Illegal State",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorDetails> handleValidationException(ValidationException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Incorrect format of request",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails> handleValidationException(MethodArgumentNotValidException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Incorrect format of request",
				e.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
						.collect(Collectors.toList()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorDetails> handleAuthenticationException(AuthenticationException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(),
				"Access denied", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(InvalidJwtTokenException.class)
	public ResponseEntity<ErrorDetails> handleInvalidJwtTokenException(InvalidJwtTokenException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorDetails> handleBadCredentialsException(BadCredentialsException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Incorrect email address or password",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleNotFoundEmailException(UsernameNotFoundException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), "Incorrect email address or password",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
}
