package com.uet.book_a_book.exception.book;

public class NotEnoughQuantityException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotEnoughQuantityException(String message) {
		super(message);
	}
}
