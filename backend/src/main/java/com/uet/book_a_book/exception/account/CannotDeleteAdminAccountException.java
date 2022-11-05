package com.uet.book_a_book.exception.account;

public class CannotDeleteAdminAccountException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CannotDeleteAdminAccountException(String message) {
		super(message);
	}
}
