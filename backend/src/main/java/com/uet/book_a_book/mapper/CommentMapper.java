package com.uet.book_a_book.mapper;

import com.uet.book_a_book.dto.comment.CommentDTO;
import com.uet.book_a_book.entity.Comment;

public interface CommentMapper {
	CommentDTO mapToCommentDTO(Comment comment);
}
