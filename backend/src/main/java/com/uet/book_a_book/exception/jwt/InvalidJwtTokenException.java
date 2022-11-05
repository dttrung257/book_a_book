package com.uet.book_a_book.exception.jwt;

public class InvalidJwtTokenException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidJwtTokenException(String message) {
		super(message);
	}
}
