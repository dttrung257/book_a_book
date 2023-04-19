package com.uet.book_a_book.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.uet.book_a_book.dtos.comment.CommentDTO;
import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.models.Book;
import com.uet.book_a_book.models.Comment;
import com.uet.book_a_book.mapper.CommentMapper;
import com.uet.book_a_book.repositories.CommentRepository;

@Component
@RequiredArgsConstructor
public class CommentMapperImpl implements CommentMapper {
	private final CommentRepository commentRepository;

	@Override
	public CommentDTO mapToCommentDTO(Comment comment) {
		CommentDTO commentDTO = new CommentDTO();
		commentDTO.setId(comment.getId());
		commentDTO.setStar(comment.getStar());
		commentDTO.setContent(comment.getContent());
		Book book = commentRepository.findBookByCommentId(comment.getId()).orElse(null);
		if (book == null) {
			commentDTO.setBookId(null);
			commentDTO.setBookName(null);
		} else {
			commentDTO.setBookId(book.getId());
			commentDTO.setBookName(book.getName());
		}
		AppUser user = commentRepository.findUserByCommentId(comment.getId()).orElse(null);
		if (user == null) {
			commentDTO.setUserId(null);
			commentDTO.setFullName(null);
			commentDTO.setAvatar(null);
		} else {
			commentDTO.setFullName(user.getFirstName() + " " + user.getLastName());
			commentDTO.setUserId(user.getId());
			commentDTO.setAvatar(user.getAvatar());
		}
		commentDTO.setCreatedAt(comment.getCreatedAt());
		commentDTO.setUpdatedAt(comment.getUpdatedAt());
		return commentDTO;
	}

}
