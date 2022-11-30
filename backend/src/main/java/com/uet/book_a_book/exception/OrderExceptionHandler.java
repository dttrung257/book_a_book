package com.uet.book_a_book.exception;

import java.util.Date;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.uet.book_a_book.exception.book.NotEnoughQuantityException;
import com.uet.book_a_book.exception.order.CannotCancelOrderException;
import com.uet.book_a_book.exception.order.CannotDeleteShippingOrderException;
import com.uet.book_a_book.exception.order.CannotDeleteSuccessOrderException;
import com.uet.book_a_book.exception.order.NotFoundOrderException;
import com.uet.book_a_book.exception.order.NotFoundOrderStatusException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OrderExceptionHandler {
	@ExceptionHandler(NotFoundOrderException.class)
	public ResponseEntity<ErrorDetails> handleNotFoundOrderException(NotFoundOrderException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Not found order", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(CannotCancelOrderException.class)
	public ResponseEntity<ErrorDetails> handleCannotCancelOrderException(CannotCancelOrderException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Orders can only be canceled in pending status", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(NotFoundOrderStatusException.class)
	public ResponseEntity<ErrorDetails> handleNotFoundOrderStatusException(NotFoundOrderStatusException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Not found order status", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(NotEnoughQuantityException.class)
	public ResponseEntity<ErrorDetails> handleNotEnoughQuantityException(NotEnoughQuantityException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Not enough quantity", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(CannotDeleteShippingOrderException.class)
	public ResponseEntity<ErrorDetails> handleCannotDeleteShippingOrderException(CannotDeleteShippingOrderException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Cannot delete shipping order", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(CannotDeleteSuccessOrderException.class)
	public ResponseEntity<ErrorDetails> handleCannotDeleteSuccessOrderException(CannotDeleteSuccessOrderException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Cannot delete success order", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
}
