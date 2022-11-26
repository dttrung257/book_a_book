package com.uet.book_a_book.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

import com.uet.book_a_book.dto.book.NewBook;
import com.uet.book_a_book.dto.book.UpdateBookStatus;
import com.uet.book_a_book.service.BookService;

@RestController
@RequestMapping("/api")
@Validated
public class BookController {
	@Autowired
	private BookService bookService;

	@GetMapping("/books")
	ResponseEntity<Object> getAllBooks(
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getAllBooks(page, size));
	}

	@GetMapping("books/{id}")
	ResponseEntity<Object> getBookById(
			@PathVariable(name = "id", required = true) @Min(value = 1L) Long id) {
		return ResponseEntity.ok(bookService.getBookById(id));
	}
	
	@GetMapping("books/name")
	ResponseEntity<Object> getBooksByName(
			@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByName(name, page, size));
	}
	
	@GetMapping("books/category")
	ResponseEntity<Object> getBooksByCategory(
			@RequestParam(name = "category", required = true) String category,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByCategory(category, page, size));
	}
	
	@GetMapping("books/price")
	ResponseEntity<Object> getBooksByPrice(
			@RequestParam(name= "from", required = false, defaultValue = "0") @DecimalMin(value = "0.0") Double fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = "10000000000") @DecimalMin(value = "0.1") Double toPrice,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByPrice(fromPrice, toPrice, page, size));
	}
	
	@GetMapping("/books/rating")
	ResponseEntity<Object> getBooksByRating(
			@RequestParam(name = "rating", required = false, defaultValue = "0") @Min(value = 0) @Max(value = 5) Integer rating,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByRating(rating, page, size));
	}
	
	@GetMapping("/books/best_selling")
	ResponseEntity<Object> getBooksByBestSelling(
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByBestSelling(page, size));
	}
	
	@GetMapping("/books/filter")
	ResponseEntity<Object> getBooksByFilter(
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "category", required = false, defaultValue = "") String category,
			@RequestParam(name= "from", required = false, defaultValue = "0") @DecimalMin(value = "0.0") Double fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = "10000000000") @DecimalMin(value = "0.1") Double toPrice,
			@RequestParam(name = "rating", required = false, defaultValue = "0") @Min(value = 0) @Max(value = 5) Integer rating,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByFilter(name, category, fromPrice, toPrice, rating, page, size));
	}
	
	@GetMapping("/books/cart")
	ResponseEntity<Object> getBooksInCart(@RequestParam(name = "ids", required = true) Long... ids) {
		Set<Long> idList = new HashSet<>(Arrays.asList(ids));
		return ResponseEntity.ok(bookService.getBooksInCart(idList));
	}
	
	@PostMapping("/manage/books")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Object> addBook(@Valid @RequestBody NewBook newBook) {
		return ResponseEntity.ok(bookService.addBook(newBook));
	}
	
	@PutMapping("/manage/books/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Object> updateBook(
			@Valid @RequestBody NewBook updateBook,
			@PathVariable(name = "id", required = true) @Min(value = 1L) Long id) {
		return ResponseEntity.ok(bookService.updateBook(updateBook, id));
	}
	
	@PutMapping("/manage/books/{id}/status")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Object> updateStatus(
			@Valid @RequestBody UpdateBookStatus bookStatus,
			@PathVariable(name = "id", required = true) @Min(value = 1L) Long id) {
		return ResponseEntity.ok(bookService.updateStatus(id, bookStatus.getStopSelling()));
	}
	
	@DeleteMapping("/manage/books/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Object> deleteBook(
			@PathVariable(name = "id", required = true) @Min(value = 1L) Long id) {
		bookService.deleteBook(id);
		return ResponseEntity.ok("Delete book id: " + id + " successfully");
	}
	
}
