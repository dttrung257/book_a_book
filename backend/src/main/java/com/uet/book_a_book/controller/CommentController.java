package com.uet.book_a_book.controller;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.uet.book_a_book.dto.comment.NewComment;
import com.uet.book_a_book.service.CommentService;

@RestController
@RequestMapping("/api")
@Validated
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	@GetMapping("/comments/user_comment")
	ResponseEntity<Object> getUserComment(
			@RequestParam(name = "book_id", required = true) 
			@Min(value = 1L) Long bookId) {
		return ResponseEntity.ok(commentService.getUserComment(bookId));
	}
	
	@GetMapping("/comments/other_comment")
	ResponseEntity<Object> getOtherComments(
			@RequestParam(name = "book_id", required = true) 
			@Min(value = 1L) Long bookId,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) {
		return ResponseEntity.ok(commentService.getOtherComments(bookId, page, size));
	}
	
	@PostMapping("/comments")
	ResponseEntity<Object> addComment(
			@Valid @RequestBody NewComment newComment) {
		return ResponseEntity.ok(commentService.addComment(newComment));
	}
	
	@PutMapping("/comments")
	ResponseEntity<Object> updateComment(
			@Valid @RequestBody NewComment updateComment) {
		return ResponseEntity.ok(commentService.updateComment(updateComment));
	}
	
	@DeleteMapping("/comments")
	ResponseEntity<Object> deleteComment(
			@RequestParam(name = "book_id", required = true) 
			@Min(value = 1L) Long bookId) {
		commentService.deleteComment(bookId);
		return ResponseEntity.ok("Delete comment successfully");
	}
	
	@GetMapping("/manage/comments")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	ResponseEntity<Object> getAllComments(
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) {
		return ResponseEntity.ok(commentService.getAllComments(page, size));
	}
	
	@GetMapping("/manage/comments/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	ResponseEntity<Object> getCommentById(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") String id) {
		return ResponseEntity.ok(commentService.getCommentById(UUID.fromString(id)));
	}
	
	@DeleteMapping("/manage/comments/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	ResponseEntity<Object> deleteCommentByAdmin(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") String id) {
		commentService.deleteCommentByAdmin(UUID.fromString(id));
		return ResponseEntity.ok("Delete comment id:" + id + " successfully");
	}
}
