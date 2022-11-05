package com.uet.book_a_book.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.book.NewBook;
import com.uet.book_a_book.dto.book.UpdateBook;
import com.uet.book_a_book.service.BookService;

@RestController
@RequestMapping("/api")
@Validated
public class BookController {
	@Autowired
	private BookService bookService;

	@GetMapping("/book/fetch_books")
	ResponseEntity<Object> fetchAllBooks(
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(bookService.findAll(Integer.parseInt(page), Integer.parseInt(size)));
	}

	@GetMapping("book/fetch_by_id/{id}")
	ResponseEntity<Object> findBookById(
			@PathVariable("id") 
			@Min(value = 1, message = "Id must be in integer format and greater than or equal to 1") String id) {
		return ResponseEntity.ok(bookService.findById(Long.parseLong(id)));
	}
	
	@GetMapping("book/fetch_by_name")
	ResponseEntity<Object> findBookByName(
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(bookService.findByName(name, Integer.parseInt(page), Integer.parseInt(size)));
	}
	
	@GetMapping("book/fetch_by_category")
	ResponseEntity<Object> findBookByCategory(
			@RequestParam(name = "category", required = false, defaultValue = "") String category,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(bookService.findByCategory(category, Integer.parseInt(page), Integer.parseInt(size)));
	}
	
	@GetMapping("book/fetch_by_price")
	ResponseEntity<Object> findByPrice(
			@RequestParam(name= "from", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Lower price must be in numeric format greater than or equal to 0") String fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = "1000000") 
			@Min(value = 1, message = "Higher price must be in numeric format greater than or equal to 1") String toPrice,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(bookService.findByPrice(Double.parseDouble(fromPrice), Double.parseDouble(toPrice), Integer.parseInt(page), Integer.parseInt(size)));
	}
	
	@GetMapping("/book/fetch_by_rating")
	ResponseEntity<Object> findByRating(
			@RequestParam(name = "rating", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Rating must be in integer format greater than or equal to 0") 
			@Max(value = 5, message = "Rating must be in integer format lesser than or equal to 5") String rating,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(bookService.findByRating(Integer.parseInt(rating), Integer.parseInt(page), Integer.parseInt(size)));
	}
	
	@GetMapping("/book/fetch_by_best_selling")
	ResponseEntity<Object> findByBestSelling(
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(bookService.findByBestSelling(Integer.parseInt(page), Integer.parseInt(size)));
	}
	
	@PostMapping("/manage_book/add_book")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Object> addBook(@Valid @RequestBody NewBook newBook) {
		return ResponseEntity.ok(bookService.addBook(newBook));
	}
	
	@PostMapping("/manage_book/update_book")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Object> updateBook(@Valid @RequestBody UpdateBook updateBook) {
		return ResponseEntity.ok(bookService.updateBook(updateBook));
	}
	
	@GetMapping("/manage_book/delete_book/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Object> deleteBook(
			@PathVariable("id") 
			@Min(value = 1, message = "Id must be in integer format and greater than or equal to 1") String id) {
		bookService.deleteBook(Long.parseLong(id));
		return ResponseEntity.ok("Delete book id " + id + " successfully");
	}
}
