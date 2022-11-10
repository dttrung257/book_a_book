package com.uet.book_a_book.exception.account;

public class IncorrectOldPasswordException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IncorrectOldPasswordException(String message) {
		super(message);
	}
}
