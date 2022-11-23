package com.uet.book_a_book.exception.order;

public class CannotDeleteShippingOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotDeleteShippingOrderException(String message) {
		super(message);
	}
}
