package com.uet.book_a_book.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.uet.book_a_book.exception.account.AccountAlreadyActivatedException;
import com.uet.book_a_book.exception.account.AccountAlreadyExistsException;
import com.uet.book_a_book.exception.account.AccountNotActivatedException;
import com.uet.book_a_book.exception.account.CannotDeleteAdminAccountException;
import com.uet.book_a_book.exception.account.CannotLockAdminAccountException;
import com.uet.book_a_book.exception.account.CannotResetPasswordException;
import com.uet.book_a_book.exception.account.EmailNotExistsOnTheInternetException;
import com.uet.book_a_book.exception.account.EmailSendingErrorException;
import com.uet.book_a_book.exception.account.IncorrectEmailVerificationCodeException;
import com.uet.book_a_book.exception.account.IncorrectOldPasswordException;
import com.uet.book_a_book.exception.account.IncorrectResetPasswordCodeException;
import com.uet.book_a_book.exception.account.IncorrectResetTokenException;
import com.uet.book_a_book.exception.account.LockedAccountException;
import com.uet.book_a_book.exception.account.NotFoundAccountException;
import com.uet.book_a_book.exception.account.NotFoundGenderException;
import com.uet.book_a_book.exception.account.NotFoundResetPasswordTokenException;
import com.uet.book_a_book.exception.account.NotFoundUserStatusException;

@ControllerAdvice
public class AccountExceptionHandler {
	@ExceptionHandler(NotFoundAccountException.class)
	ResponseEntity<Object> handleNotFoundAccountException(NotFoundAccountException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), "Not found account",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
		
	}
	
	@ExceptionHandler(NotFoundUserStatusException.class)
	ResponseEntity<Object> handleNotFoundUserStatusException(NotFoundUserStatusException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), "Not found user status",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
		
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
	
	@ExceptionHandler(IncorrectOldPasswordException.class)
	ResponseEntity<Object> handleIncorrectOldPasswordException(IncorrectOldPasswordException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"The current password is incorrect, the password cannot be changed", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(IncorrectResetPasswordCodeException.class)
	ResponseEntity<Object> handleIncorrectResetPasswordCodeException(IncorrectResetPasswordCodeException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Incorrect reset password verification code", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(IncorrectResetTokenException.class)
	ResponseEntity<Object> handleIncorrectResetTokenException(IncorrectResetTokenException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Incorrect reset password token", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(NotFoundResetPasswordTokenException.class)
	ResponseEntity<Object> handleNotFoundResetPasswordTokenException(NotFoundResetPasswordTokenException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Reset password token does not exists", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(CannotLockAdminAccountException.class)
	ResponseEntity<Object> handleCannotLockAdminAccountException(CannotLockAdminAccountException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(),
				"Cannot lock admin account", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(CannotDeleteAdminAccountException.class)
	ResponseEntity<Object> handleCannotDeleteAdminAccountException(CannotDeleteAdminAccountException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(),
				"Cannot delete admin account", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(CannotResetPasswordException.class)
	ResponseEntity<Object> handleCannotResetPasswordException(CannotResetPasswordException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(),
				"Cannot reset password of another admin account", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(NotFoundGenderException.class)
	ResponseEntity<Object> handleNotFoundGenderException(NotFoundGenderException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Not found gender", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
}
