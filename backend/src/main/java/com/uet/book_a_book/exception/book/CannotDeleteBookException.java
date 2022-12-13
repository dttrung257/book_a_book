package com.uet.book_a_book.exception.book;

public class CannotDeleteBookException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CannotDeleteBookException(String message) {
		super(message);
	}
}
