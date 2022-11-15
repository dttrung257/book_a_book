package com.uet.book_a_book.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.comment.CommentDTO;
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
	
	private CommentDTO commentToCommentDTO(Comment comment) {
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
			commentDTO.setEmail(null);
		} else {
			commentDTO.setEmail(user.getEmail());
			commentDTO.setUserId(user.getId());
		}
		return commentDTO;
	}
	
	@Override
	public CommentDTO getUserComment(Long bookId) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book with id: " + bookId);
		}
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<CommentDTO> comments = commentRepository.findUserComment(user.getId(), bookId)
				.stream().map(c -> commentToCommentDTO(c)).collect(Collectors.toList());
		if (comments.isEmpty()) {
			return null;
		}
		return comments.get(0);
	}
	
	@Override
	public Page<CommentDTO> getOtherComments(Long bookId, Integer page, Integer size) {
		Book book = bookRepository.findById(bookId).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book with id: " + bookId);
		}
		Pageable pageable = PageRequest.of(page, size);
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<CommentDTO> comments = commentRepository.findOtherUserComments(user.getId(), bookId)
				.stream().map(c -> commentToCommentDTO(c)).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), comments.size());
		if (start <= comments.size()) {
			return new PageImpl<>(comments.subList(start, end), pageable, comments.size());
		}
		return new PageImpl<>(comments, pageable, comments.size());
	}

	@Override
	public CommentDTO addComment(NewComment newComment) {
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
		return commentToCommentDTO(comment);
	}

	@Override
	public CommentDTO updateComment(NewComment updateComment) {
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
			return commentToCommentDTO(comment);
		}
		comment.setContent(updateComment.getContent());
		comment.setStar(updateComment.getStar());
		comment.setUpdatedAt(new Date());
		commentRepository.save(comment);
		double newRating = commentRepository.calculateRateOfBook(updateComment.getBookId());
		book.setRating(Math.ceil(newRating * 10) / 10);
		bookRepository.save(book);
		return commentToCommentDTO(comment);
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
	public void deleteCommentByAdmin(UUID id) {
		Book book = commentRepository.findBookByCommentId(id).orElse(null);
		if (book == null) {    
			throw new NotFoundBookException("Not found book with comment id: " + id);
		}
		Comment comment = commentRepository.findById(id).orElse(null);
		if (comment == null) {
			throw new NotFoundCommentException("Not found comment id: " + id);
		} else {
			commentRepository.delete(comment);
		}
		Long numComment = bookRepository.countComment(book.getId());
		if (numComment == 0) {
			book.setRating(null);
		} else {
			double newRating = commentRepository.calculateRateOfBook(book.getId());
			book.setRating(Math.ceil(newRating * 10) / 10);
		}
		bookRepository.save(book);
		
	}

	@Override
	public Page<CommentDTO> getAllComments(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<CommentDTO> comments = commentRepository.findAll()
				.stream().map(c -> commentToCommentDTO(c)).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), comments.size());
		if (start <= comments.size()) {
			return new PageImpl<>(comments.subList(start, end), pageable, comments.size());
		}
		return new PageImpl<>(comments, pageable, comments.size());
	}

	@Override
	public CommentDTO getCommentById(UUID id) {
		Comment comment = commentRepository.findById(id).orElse(null);
		if (comment == null) {
			throw new NotFoundCommentException("Not found comment id: " + id);
		}
		return commentToCommentDTO(comment);
	}
	
}
