package com.uet.book_a_book.exception;

import java.util.Date;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

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
import com.uet.book_a_book.exception.account.NotFoundResetPasswordTokenException;
import com.uet.book_a_book.exception.book.BookAlreadyExistsException;
import com.uet.book_a_book.exception.book.NotFoundBookException;
import com.uet.book_a_book.exception.comment.CommentAlreadyExistsException;
import com.uet.book_a_book.exception.comment.NotFoundCommentException;
import com.uet.book_a_book.exception.comment.UserHasNotCommentedYetException;
import com.uet.book_a_book.exception.jwt.InvalidJwtTokenException;
import com.uet.book_a_book.exception.order.CannotCancelOrderException;
import com.uet.book_a_book.exception.order.CannotChangeOrderStatusException;
import com.uet.book_a_book.exception.order.NotFoundOrderException;
import com.uet.book_a_book.exception.order.NotFoundOrderStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	ResponseEntity<ErrorDetails> handleGlobalException(Exception e, WebRequest webRequest) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				e.getMessage(), webRequest.getDescription(false));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Description", "Error")
				.body(errorDetails);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	ResponseEntity<Object> handleAuthenticationException(AuthenticationException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.FORBIDDEN.value(),
				"Access denied", e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
	}
	
	@ExceptionHandler(InvalidJwtTokenException.class)
	ResponseEntity<Object> handleInvalidJwtTokenException(InvalidJwtTokenException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.UNAUTHORIZED.value(),
				"Unauthorized", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
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
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Incorrect format of request",
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
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), "Incorrect format of request",
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
	
	@ExceptionHandler(IncorrectOldPasswordException.class)
	ResponseEntity<Object> handleIncorrectOldPasswordException(IncorrectOldPasswordException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"The current password is incorrect, the password cannot be reversed", e.getMessage());
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
	
	@ExceptionHandler(NotFoundBookException.class)
	ResponseEntity<Object> handleNotFoundBookException(NotFoundBookException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Not found book", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(BookAlreadyExistsException.class)
	ResponseEntity<Object> handleBookAlreadyExistsException(BookAlreadyExistsException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Book already exists", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(CommentAlreadyExistsException.class) 
	ResponseEntity<Object> handleCommentAlreadyExistsException(CommentAlreadyExistsException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"User have already commented on this book", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(UserHasNotCommentedYetException.class) 
	ResponseEntity<Object> handleUserHasNotCommentedYetException(UserHasNotCommentedYetException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"User has not commented yet", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(NotFoundCommentException.class)
	ResponseEntity<Object> handleNotFoundCommentException(NotFoundCommentException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Not found comment", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(NotFoundOrderException.class)
	ResponseEntity<Object> handleNotFoundOrderException(NotFoundOrderException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Not found order", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(CannotCancelOrderException.class)
	ResponseEntity<Object> handleCannotCancelOrderException(CannotCancelOrderException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Orders can only be canceled in pending status", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(CannotChangeOrderStatusException.class)
	ResponseEntity<Object> handleCannotChangeOrderStatusException(CannotChangeOrderStatusException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"The status cannot be changed once the order has been successfully delivered", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(NotFoundOrderStatusException.class)
	ResponseEntity<Object> handleNotFoundOrderStatusException(NotFoundOrderStatusException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Not found order status", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
}
