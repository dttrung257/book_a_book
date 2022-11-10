package com.uet.book_a_book.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.comment.NewComment;
import com.uet.book_a_book.entity.Comment;

public interface CommentService {
	Comment fetchUserComment(Long bookId);
	Page<Comment> fetchOtherComment(Long bookId, Integer page, Integer size);
	Comment addComment(NewComment newComment);
	Comment updateComment(NewComment updateComment);
	void deleteComment(Long bookId);
	void deleteCommentFromAdmin(UUID commentId, Long bookId);
}
