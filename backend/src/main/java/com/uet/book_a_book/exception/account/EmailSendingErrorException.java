package com.uet.book_a_book.exception.account;

public class EmailSendingErrorException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmailSendingErrorException(String message) {
		super(message);
	}
}
