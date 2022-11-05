package com.uet.book_a_book.service;

import java.util.List;
import java.util.UUID;

import com.uet.book_a_book.dto.comment.NewComment;
import com.uet.book_a_book.entity.Comment;

public interface CommentService {
	Comment fetchUserComment(Long bookId);
	List<Comment> fetchOtherComment(Long bookId);
	Comment addComment(NewComment newComment);
	Comment updateComment(NewComment updateComment);
	void deleteComment(Long bookId);
	void deleteCommentFromAdmin(UUID commentId, Long bookId);
}
