package com.uet.book_a_book.exception.account;

public class CannotLockAdminAccountException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotLockAdminAccountException(String message) {
		super(message);
	}
	
}
