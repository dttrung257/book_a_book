package com.uet.book_a_book.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.comment.CommentDTO;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.entity.Comment;
import com.uet.book_a_book.exception.book.NotFoundBookException;
import com.uet.book_a_book.exception.comment.CommentAlreadyExistsException;
import com.uet.book_a_book.exception.comment.NotFoundCommentException;
import com.uet.book_a_book.exception.comment.UserHasNotCommentedYetException;
import com.uet.book_a_book.repository.BookRepository;
import com.uet.book_a_book.repository.CommentRepository;
import com.uet.book_a_book.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public Comment getUserComment(Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book with id: " + bookId);
		}
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Comment> comments = commentRepository.findUserComment(user.getId(), bookId);
		if (comments.isEmpty()) {
			return null;
		}
		return comments.get(0);
	}
	
	@Override
	public Page<Comment> getOtherComments(Long bookId, Integer page, Integer size) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book with id: " + bookId);
		}
		Pageable pageable = PageRequest.of(page, size);
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Comment> comments = commentRepository.findOtherUserComments(user.getId(), bookId);
		return new PageImpl<>(comments, pageable, comments.size());
	}

	@Override
	public Comment addComment(CommentDTO newComment) {
		Book book = bookRepository.findById(newComment.getBookId()).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book with id: " + newComment.getBookId());
		}
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Comment> comments = commentRepository.findUserComment(user.getId(), newComment.getBookId());
		if (!comments.isEmpty()) {
			throw new CommentAlreadyExistsException("User " + user.getEmail() + " have already commented on the book id " + book.getId());
		}
		Comment comment = new Comment();
		comment.setContent(newComment.getContent());
		comment.setStar(newComment.getStar());
		comment.setCreatedAt(new Date());
		comment.setBook(book);
		comment.setUser(user);
		if (book.getRating() == null) {
			book.setRating(Double.valueOf(newComment.getStar()));
		} else {
			Long numComment = bookRepository.countComment(book.getId());
			double newRating = (book.getRating() * numComment + newComment.getStar()) / (numComment + 1);
			book.setRating(Math.ceil(newRating * 10) / 10);
		}
		commentRepository.save(comment);
		bookRepository.save(book);
		return comment;
	}

	@Override
	public Comment updateComment(CommentDTO updateComment) {
		Book book = bookRepository.findById(updateComment.getBookId()).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book with id: " + updateComment.getBookId());
		}
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Comment> comments = commentRepository.findUserComment(user.getId(), updateComment.getBookId());
		if (comments.isEmpty()) {
			throw new UserHasNotCommentedYetException("User " + user.getEmail() + " has not commented on the book id " + book.getId());
		}
		
		Comment comment = comments.get(0);
		if (updateComment.getContent().equals(comment.getContent()) && updateComment.getStar() == comment.getStar()) {
			return comment;
		}
		comment.setContent(updateComment.getContent());
		comment.setStar(updateComment.getStar());
		comment.setUpdatedAt(new Date());
		commentRepository.save(comment);
		double newRating = commentRepository.calculateRateOfBook(updateComment.getBookId());
		book.setRating(Math.ceil(newRating * 10) / 10);
		bookRepository.save(book);
		return comment;
	}

	@Override
	public void deleteComment(Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book with id: " + bookId);
		}
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Comment> comments = commentRepository.findUserComment(user.getId(), bookId);
		if (comments.isEmpty()) {
			throw new UserHasNotCommentedYetException("User " + user.getEmail() + " has not commented on the book id " + bookId);
		}
		commentRepository.delete(comments.get(0));
		Long numComment = bookRepository.countComment(book.getId());
		if (numComment == 0) {
			book.setRating(null);
		} else {
			double newRating = commentRepository.calculateRateOfBook(bookId);
			book.setRating(Math.ceil(newRating * 10) / 10);
		}
		bookRepository.save(book);
	}

	@Override
	public void deleteCommentByAdmin(UUID commentId, Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book id: " + bookId);
		}
		if (!commentRepository.existsById(commentId)) {
			throw new NotFoundCommentException("Not found comment id: " + commentId.toString());
		}
		Comment comment = commentRepository.findByCommetIdAndBookId(commentId, bookId).orElse(null);
		if (comment == null) {
			throw new NotFoundCommentException("Not found comment id: " + commentId + " in book id: " + bookId);
		} else {
			commentRepository.delete(comment);
		}
		Long numComment = bookRepository.countComment(book.getId());
		if (numComment == 0) {
			book.setRating(null);
		} else {
			double newRating = commentRepository.calculateRateOfBook(bookId);
			book.setRating(Math.ceil(newRating * 10) / 10);
		}
		bookRepository.save(book);
		
	}
	
}
