package com.uet.book_a_book.exception.account;

public class IncorrectResetPasswordCodeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IncorrectResetPasswordCodeException(String message) {
		super(message);
	}
}
