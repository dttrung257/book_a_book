package com.uet.book_a_book.exception;

public class NotFoundAccountException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundAccountException(String message) {
		super(message);
	}
}
