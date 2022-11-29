package com.uet.book_a_book.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.comment.CommentDTO;
import com.uet.book_a_book.dto.comment.NewComment;

public interface CommentService {
	// Get comments by book id
	Page<CommentDTO> getCommentsByBookId(Long bookId, Integer page, Integer size);

	// Get user comment in book page
	CommentDTO getUserComment(Long bookId);

	// Get other user comment in book page
	Page<CommentDTO> getOtherComments(Long bookId, Integer page, Integer size);
	
	// Adm get comments
	Page<CommentDTO> getComments(Long bookId, String bookName, Date date, String fullname, Integer page, Integer size);

	// Adm get comment by id
	CommentDTO getCommentById(UUID id);
	
	// Add new comment
	CommentDTO addComment(NewComment newComment);

	// Update comment
	CommentDTO updateComment(NewComment updateComment);

	// User delete comment
	void deleteComment(Long bookId);
	
	// Adm delete comment
	void deleteCommentByAdmin(UUID id);
}
