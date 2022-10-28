package com.uet.book_a_book.exception;

public class AccountAlreadyExistsException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AccountAlreadyExistsException(String message) {
		super(message);
	}
}
