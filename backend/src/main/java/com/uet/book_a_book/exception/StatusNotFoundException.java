package com.uet.book_a_book.exception;

public class StatusNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StatusNotFoundException(String message) {
		super(message);
	}
}
