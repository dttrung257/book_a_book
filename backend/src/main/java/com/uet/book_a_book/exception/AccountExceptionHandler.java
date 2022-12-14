package com.uet.book_a_book.exception;

import java.util.Date;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AccountExceptionHandler {
	@ExceptionHandler(NotFoundAccountException.class)
	public ResponseEntity<ErrorDetails> handleNotFoundAccountException(NotFoundAccountException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), "Not found account",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
		
	}
	
	@ExceptionHandler(NotFoundUserStatusException.class)
	public ResponseEntity<ErrorDetails> handleNotFoundUserStatusException(NotFoundUserStatusException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(), "Not found user status",
				e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
		
	}

	@ExceptionHandler(AccountNotActivatedException.class)
	public ResponseEntity<ErrorDetails> handleAccountNotActivatedException(AccountNotActivatedException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(),
				"Account not activated", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
	}
	
	@ExceptionHandler(LockedAccountException.class)
	public ResponseEntity<ErrorDetails> handleLockedAccountException(LockedAccountException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(),
				"Account has been locked", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(EmailSendingErrorException.class)
	public ResponseEntity<ErrorDetails> handleEmailSendingErrorException(EmailSendingErrorException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Fail to send email", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
	}
	
	@ExceptionHandler(EmailNotExistsOnTheInternetException.class)
	public ResponseEntity<ErrorDetails> handleEmailNotExistsOnTheInternetException(EmailNotExistsOnTheInternetException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Email does not exist on the internet", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
	}
	
	@ExceptionHandler(AccountAlreadyExistsException.class)
	public ResponseEntity<ErrorDetails> handleAccountAlreadyExistsException(AccountAlreadyExistsException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Account already exists", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(AccountAlreadyActivatedException.class)
	public ResponseEntity<ErrorDetails> handleAccountAlreadyActivatedException(AccountAlreadyActivatedException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Account already activated", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(IncorrectEmailVerificationCodeException.class)
	public ResponseEntity<ErrorDetails> handleIncorrectEmailVerificationCodeException(IncorrectEmailVerificationCodeException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Incorrect email verification code", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(IncorrectOldPasswordException.class)
	public ResponseEntity<ErrorDetails> handleIncorrectOldPasswordException(IncorrectOldPasswordException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"The current password is incorrect, the password cannot be changed", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(IncorrectResetPasswordCodeException.class)
	public ResponseEntity<ErrorDetails> handleIncorrectResetPasswordCodeException(IncorrectResetPasswordCodeException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Incorrect reset password verification code", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(IncorrectResetTokenException.class)
	public ResponseEntity<ErrorDetails> handleIncorrectResetTokenException(IncorrectResetTokenException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Incorrect reset password token", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(NotFoundResetPasswordTokenException.class)
	public ResponseEntity<ErrorDetails> handleNotFoundResetPasswordTokenException(NotFoundResetPasswordTokenException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Reset password token does not exists", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(CannotLockAdminAccountException.class)
	public ResponseEntity<ErrorDetails> handleCannotLockAdminAccountException(CannotLockAdminAccountException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(),
				"Cannot lock admin account", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(CannotDeleteAdminAccountException.class)
	public ResponseEntity<ErrorDetails> handleCannotDeleteAdminAccountException(CannotDeleteAdminAccountException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(),
				"Cannot delete admin account", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(CannotResetPasswordException.class)
	public ResponseEntity<ErrorDetails> handleCannotResetPasswordException(CannotResetPasswordException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(),
				"Cannot reset password of another admin account", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(NotFoundGenderException.class)
	public ResponseEntity<ErrorDetails> handleNotFoundGenderException(NotFoundGenderException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Not found gender", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
}
