package com.uet.book_a_book.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.comment.CommentDTO;
import com.uet.book_a_book.dto.comment.NewComment;

public interface CommentService {
	Page<CommentDTO> getAllComments(Long bookId, Integer page, Integer size);
	CommentDTO getUserComment(Long bookId);
	Page<CommentDTO> getOtherComments(Long bookId, Integer page, Integer size);
	CommentDTO addComment(NewComment newComment);
	CommentDTO updateComment(NewComment updateComment);
	void deleteComment(Long bookId);
	void deleteCommentByAdmin(UUID id);
	Page<CommentDTO> getAllComments(Integer page, Integer size);
	CommentDTO getCommentById(UUID id);
}
