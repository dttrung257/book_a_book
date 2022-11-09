package com.uet.book_a_book.exception.order;

public class NotFoundOrderStatusException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundOrderStatusException(String message) {
		super(message);
	}
}
