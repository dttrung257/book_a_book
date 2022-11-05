package com.uet.book_a_book.exception.comment;

public class CommentAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CommentAlreadyExistsException(String message) {
		super(message);
	}
}
