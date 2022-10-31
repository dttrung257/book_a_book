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
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				e.getMessage(), List.of(webRequest.getDescription(false)));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Description", "Error")
				.body(errorDetails);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	ResponseEntity<Object> handleNotFoundEmailException(UsernameNotFoundException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), "Incorrect email address or password",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(NotFoundAccountException.class)
	ResponseEntity<Object> handleNotFoundAccountException(NotFoundAccountException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), "Not found account",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
		
	}

	@ExceptionHandler(BadCredentialsException.class)
	ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Incorrect email address or password",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Validation Error",
				e.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
						.collect(Collectors.toList()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}

	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<Object> handleIllegalStateException(IllegalStateException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Illegal State",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}

	@ExceptionHandler(ValidationException.class)
	ResponseEntity<Object> handleValidationException(ValidationException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Validation Error",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}

	@ExceptionHandler(AccountNotActivatedException.class)
	ResponseEntity<Object> handleAccountNotActivatedException(AccountNotActivatedException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(),
				"Account not activated", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
	}
	
	@ExceptionHandler(LockedAccountException.class)
	ResponseEntity<Object> handleLockedAccountException(LockedAccountException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(),
				"Account has been locked", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
	}
	
	@ExceptionHandler(EmailSendingErrorException.class)
	ResponseEntity<Object> handleEmailSendingErrorException(EmailSendingErrorException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Fail to send email", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
	}
	
	@ExceptionHandler(EmailNotExistsOnTheInternetException.class)
	ResponseEntity<Object> handleEmailNotExistsOnTheInternetException(EmailNotExistsOnTheInternetException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Email does not exist on the internet", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
	}
	
	@ExceptionHandler(AccountAlreadyExistsException.class)
	ResponseEntity<Object> handleAccountAlreadyExistsException(AccountAlreadyExistsException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Account already exists", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(AccountAlreadyActivatedException.class)
	ResponseEntity<Object> handleAccountAlreadyActivatedException(AccountAlreadyActivatedException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Account already activated", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(IncorrectEmailVerificationCodeException.class)
	ResponseEntity<Object> handleIncorrectEmailVerificationCodeException(IncorrectEmailVerificationCodeException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Incorrect email verification code", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	
}
