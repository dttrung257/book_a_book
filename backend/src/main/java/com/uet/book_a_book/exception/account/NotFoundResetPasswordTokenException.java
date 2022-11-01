package com.uet.book_a_book.exception.account;

public class NotFoundResetPasswordTokenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundResetPasswordTokenException(String message) {
		super(message);
	}
}
