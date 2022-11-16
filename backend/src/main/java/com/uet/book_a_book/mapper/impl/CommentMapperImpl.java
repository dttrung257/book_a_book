package com.uet.book_a_book.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uet.book_a_book.dto.comment.CommentDTO;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.entity.Comment;
import com.uet.book_a_book.mapper.CommentMapper;
import com.uet.book_a_book.repository.CommentRepository;

@Component
public class CommentMapperImpl implements CommentMapper {
	@Autowired
	private CommentRepository commentRepository;

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
		return commentDTO;
	}

}
