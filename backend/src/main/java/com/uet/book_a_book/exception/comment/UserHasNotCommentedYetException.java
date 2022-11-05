package com.uet.book_a_book.exception.comment;

public class UserHasNotCommentedYetException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserHasNotCommentedYetException(String message) {
		super(message);
	}
}
