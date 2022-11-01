package com.uet.book_a_book.exception.account;

public class IncorrectEmailVerificationCodeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IncorrectEmailVerificationCodeException(String message) {
		super(message);
	}
}
