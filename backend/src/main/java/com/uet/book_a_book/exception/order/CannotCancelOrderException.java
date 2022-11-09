package com.uet.book_a_book.exception.order;

public class CannotCancelOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CannotCancelOrderException(String message) {
		super(message);
	}
}
