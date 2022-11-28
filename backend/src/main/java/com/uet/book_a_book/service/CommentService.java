package com.uet.book_a_book.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.comment.CommentDTO;
import com.uet.book_a_book.dto.comment.NewComment;

public interface CommentService {
	//
	Page<CommentDTO> getAllComments(Long bookId, Integer page, Integer size);

	CommentDTO getUserComment(Long bookId);

	Page<CommentDTO> getOtherComments(Long bookId, Integer page, Integer size);
	
	// For admins
	Page<CommentDTO> getAllComments(Integer page, Integer size);
	
	Page<CommentDTO> getCommentsByFilters(Long bookId, String bookName, Date date, String fullname, Integer page, Integer size);

	CommentDTO getCommentById(UUID id);
	
	//
	CommentDTO addComment(NewComment newComment);

	CommentDTO updateComment(NewComment updateComment);

	void deleteComment(Long bookId);
	
	// For admins
	void deleteCommentByAdmin(UUID id);
}
