package com.uet.book_a_book.mapper;

import com.uet.book_a_book.dtos.comment.CommentDTO;
import com.uet.book_a_book.models.Comment;

public interface CommentMapper {
	CommentDTO mapToCommentDTO(Comment comment);
}
