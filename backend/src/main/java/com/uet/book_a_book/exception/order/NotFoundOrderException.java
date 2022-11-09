package com.uet.book_a_book.exception.order;

public class NotFoundOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundOrderException(String message) {
		super(message);
	}
}
