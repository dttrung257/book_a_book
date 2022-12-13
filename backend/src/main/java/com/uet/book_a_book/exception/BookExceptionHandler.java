package com.uet.book_a_book.exception;

import java.util.Date;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.uet.book_a_book.exception.book.BookAlreadyExistsException;
import com.uet.book_a_book.exception.book.CannotDeleteBookException;
import com.uet.book_a_book.exception.book.NotFoundBookException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BookExceptionHandler {
	@ExceptionHandler(NotFoundBookException.class)
	public ResponseEntity<ErrorDetails> handleNotFoundBookException(NotFoundBookException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.NOT_FOUND.value(),
				"Not found book", e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	}
	
	@ExceptionHandler(BookAlreadyExistsException.class)
	public ResponseEntity<ErrorDetails> handleBookAlreadyExistsException(BookAlreadyExistsException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Book already exists", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
	
	@ExceptionHandler(CannotDeleteBookException.class)
	public ResponseEntity<ErrorDetails> handleCannotDeleteBookException(CannotDeleteBookException e) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(),
				"Book cannot be deleted while there is an order pending or shipping or success", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
	}
}
