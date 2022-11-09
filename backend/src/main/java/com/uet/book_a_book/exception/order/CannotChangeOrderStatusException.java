package com.uet.book_a_book.exception.order;

public class CannotChangeOrderStatusException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotChangeOrderStatusException(String message) {
		super(message);
	}
}
