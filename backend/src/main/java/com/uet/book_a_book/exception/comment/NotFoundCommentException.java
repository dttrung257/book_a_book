package com.uet.book_a_book.exception.comment;

public class NotFoundCommentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotFoundCommentException(String message) {
		super(message);
	}
}
