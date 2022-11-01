package com.uet.book_a_book.exception.account;

public class IncorrectResetTokenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IncorrectResetTokenException(String message) {
		super(message);
	}
}
