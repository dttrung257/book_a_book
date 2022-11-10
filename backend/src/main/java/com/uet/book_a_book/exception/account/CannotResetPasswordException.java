package com.uet.book_a_book.exception.account;

public class CannotResetPasswordException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotResetPasswordException(String message) {
		super(message);
	}
}
