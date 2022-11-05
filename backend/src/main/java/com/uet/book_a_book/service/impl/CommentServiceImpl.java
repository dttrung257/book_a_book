package com.uet.book_a_book.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.comment.NewComment;
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
	public Comment fetchUserComment(Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book with id: " + bookId);
		}
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Comment> comments = commentRepository.commentByUser(user.getId(), bookId);
		if (comments.isEmpty()) {
			return null;
		}
		return comments.get(0);
	}
	
	@Override
	public List<Comment> fetchOtherComment(Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book with id: " + bookId);
		}
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Comment> comments = commentRepository.commentByOtherUser(user.getId(), bookId);
		return comments;
	}

	@Override
	public Comment addComment(NewComment newComment) {
		Book book = bookRepository.findById(newComment.getBookId()).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book with id: " + newComment.getBookId());
		}
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Comment> comments = commentRepository.commentByUser(user.getId(), newComment.getBookId());
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
	public Comment updateComment(NewComment updateComment) {
		Book book = bookRepository.findById(updateComment.getBookId()).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book with id: " + updateComment.getBookId());
		}
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Comment> comments = commentRepository.commentByUser(user.getId(), updateComment.getBookId());
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
		List<Comment> comments = commentRepository.commentByUser(user.getId(), bookId);
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
	public void deleteCommentFromAdmin(UUID commentId, Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book with id: " + bookId);
		}
		if (!commentRepository.existsById(commentId)) {
			throw new NotFoundCommentException("Not found comment with id: " + commentId.toString());
		}
		commentRepository.deleteCommentFromAdmin(commentId, bookId);
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
