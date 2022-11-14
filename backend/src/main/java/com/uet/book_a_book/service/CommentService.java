package com.uet.book_a_book.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.comment.CommentDTO;
import com.uet.book_a_book.entity.Comment;

public interface CommentService {
	Comment getUserComment(Long bookId);
	Page<Comment> getOtherComments(Long bookId, Integer page, Integer size);
	Comment addComment(CommentDTO newComment);
	Comment updateComment(CommentDTO updateComment);
	void deleteComment(Long bookId);
	void deleteCommentByAdmin(UUID commentId, Long bookId);
}
