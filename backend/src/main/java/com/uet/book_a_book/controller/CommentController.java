package com.uet.book_a_book.controller;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.comment.CommentDTO;
import com.uet.book_a_book.dto.comment.NewComment;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.constant.Const;
import com.uet.book_a_book.service.CommentService;
import com.uet.book_a_book.validator.IdConstraint;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Validated
@Slf4j
public class CommentController {
	@Autowired
	private CommentService commentService;

	@GetMapping("/comments")
	public ResponseEntity<Page<CommentDTO>> getCommentsByBookId(
			@RequestParam(name = "book_id", required = true) @Min(value = 1L) Long bookId,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(commentService.getCommentsByBookId(bookId, page, size));
	}

	@GetMapping("/comments/user_comment")
	public ResponseEntity<CommentDTO> getUserComment(
			@RequestParam(name = "book_id", required = true) @Min(value = 1L) Long bookId) {
		return ResponseEntity.ok(commentService.getUserComment(bookId));
	}

	@GetMapping("/comments/other_comment")
	public ResponseEntity<Page<CommentDTO>> getOtherComments(
			@RequestParam(name = "book_id", required = true) @Min(value = 1L) Long bookId,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(commentService.getOtherComments(bookId, page, size));
	}

	@PostMapping("/comments")
	public ResponseEntity<CommentDTO> addComment(@Valid @RequestBody NewComment newComment) {
		return ResponseEntity.ok(commentService.addComment(newComment));
	}

	@PutMapping("/comments")
	public ResponseEntity<CommentDTO> updateComment(@Valid @RequestBody NewComment updateComment) {
		return ResponseEntity.ok(commentService.updateComment(updateComment));
	}

	@DeleteMapping("/comments")
	public ResponseEntity<String> deleteComment(
			@RequestParam(name = "book_id", required = true) @Min(value = 1L) Long bookId) {
		commentService.deleteComment(bookId);
		return ResponseEntity.ok("Delete comment successfully");
	}

	@GetMapping("/manage/comments")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Page<CommentDTO>> getCommentsByFilters(
			@RequestParam(name = "book_id", required = false, defaultValue = "0") Long bookId,
			@RequestParam(name = "book_name", required = false, defaultValue = "") String bookName,
			@RequestParam(name = "date") @DateTimeFormat(pattern = "dd-MM-yyyy") Optional<Date> date,
			@RequestParam(name = "fullname", required = false, defaultValue = "") String fullname,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(commentService.getComments(bookId, bookName.trim(), date.orElse(null),
				fullname.trim(), page, size));
	}

	@GetMapping("/manage/comments/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<CommentDTO> getCommentById(
			@PathVariable(name = "id", required = true) @IdConstraint String id) {
		return ResponseEntity.ok(commentService.getCommentById(UUID.fromString(id)));
	}

	@DeleteMapping("/manage/comments/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<String> deleteCommentByAdmin(
			@PathVariable(name = "id", required = true) @IdConstraint String id) {
		commentService.deleteCommentByAdmin(UUID.fromString(id));
		log.info("Admin id: {} deleted comment id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), id);
		return ResponseEntity.ok("Delete comment id: " + id + " successfully");
	}
}
