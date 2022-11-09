package com.uet.book_a_book.controller;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.comment.NewComment;
import com.uet.book_a_book.service.CommentService;

@RestController
@RequestMapping("/api")
@Validated
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/comment/fetch_user_comment")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	ResponseEntity<Object> fetchUserComment(
			@RequestParam(name = "book_id", required = false, defaultValue = "0") 
			@Min(value = 1, message = "Book id field must be in integer format greater than or equal to 1") String bookId) {
		return ResponseEntity.ok(commentService.fetchUserComment(Long.parseLong(bookId)));
	}
	
	@GetMapping("/comment/fetch_other_comment")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	ResponseEntity<Object> fetchOtherComment(
			@RequestParam(name = "book_id", required = false, defaultValue = "0") 
			@Min(value = 1, message = "Book id field must be in integer format greater than or equal to 1") String bookId) {
		return ResponseEntity.ok(commentService.fetchOtherComment(Long.parseLong(bookId)));
	}
	
	@PostMapping("/comment/add_comment")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	ResponseEntity<Object> addComment(@Valid @RequestBody NewComment newComment) {
		return ResponseEntity.ok(commentService.addComment(newComment));
	}
	
	@PostMapping("/comment/update_comment")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	ResponseEntity<Object> editComment(@Valid @RequestBody NewComment updateComment) {
		return ResponseEntity.ok(commentService.updateComment(updateComment));
	}
	
	@GetMapping("/comment/delete_comment")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	ResponseEntity<Object> deleteComment(
			@RequestParam(name = "book_id", required = false, defaultValue = "0") 
			@Min(value = 1, message = "Book id field must be in integer format greater than or equal to 1") String bookId) {
		commentService.deleteComment(Long.parseLong(bookId));
		return ResponseEntity.ok("Delete comment successfully");
	}
	
	@GetMapping("/manage_comment/delete_comment")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	ResponseEntity<Object> deleteCommentFromAdmin(
			@RequestParam(name = "book_id", required = false, defaultValue = "0") 
			@Min(value = 1, message = "Book id field must be in integer format greater than or equal to 1") String bookId,
			@RequestParam(name = "comment_id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Comment id field must in UUID format") 
			String commentId) {
		commentService.deleteCommentFromAdmin(UUID.fromString(commentId), Long.parseLong(bookId));
		return ResponseEntity.ok("Delete comment successfully");
	}
}
