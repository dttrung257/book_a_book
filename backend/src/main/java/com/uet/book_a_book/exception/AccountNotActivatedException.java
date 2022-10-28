package com.uet.book_a_book.exception;

public class AccountNotActivatedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AccountNotActivatedException(String message) {
		super(message);
	}
}
