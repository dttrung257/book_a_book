package com.uet.book_a_book.exception.account;

public class NotFoundUserStatusException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundUserStatusException(String message) {
		super(message);
	}
}
