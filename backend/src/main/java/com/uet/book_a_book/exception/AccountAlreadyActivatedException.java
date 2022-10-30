package com.uet.book_a_book.exception;

public class AccountAlreadyActivatedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AccountAlreadyActivatedException(String message) {
		super(message);
	}
}
