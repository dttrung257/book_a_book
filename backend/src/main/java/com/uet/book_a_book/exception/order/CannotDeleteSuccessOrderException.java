package com.uet.book_a_book.exception.order;

public class CannotDeleteSuccessOrderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotDeleteSuccessOrderException(String message) {
		super(message);
	}
}
