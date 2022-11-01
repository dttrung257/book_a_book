package com.uet.book_a_book.exception.account;

public class EmailNotExistsOnTheInternetException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmailNotExistsOnTheInternetException(String message) {
		super(message);
	}
}
