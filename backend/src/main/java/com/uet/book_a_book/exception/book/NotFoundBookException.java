package com.uet.book_a_book.exception.book;

public class NotFoundBookException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundBookException(String message) {
		super(message);
	}
}
