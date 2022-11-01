package com.uet.book_a_book.exception.account;

public class LockedAccountException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LockedAccountException(String message) {
		super(message);
	}
}
