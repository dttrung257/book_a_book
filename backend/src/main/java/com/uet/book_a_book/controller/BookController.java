package com.uet.book_a_book.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.entity.constant.Const;
import com.uet.book_a_book.service.BookService;

@RestController
@RequestMapping("/api")
@Validated
//@Slf4j
public class BookController {
	@Autowired
	private BookService bookService;

	@GetMapping("/books")
	public ResponseEntity<Page<Book>> getAllBooks(
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getAllBooks(page, size));
	}

	@GetMapping("books/{id}")
	public ResponseEntity<Book> getBookById(@PathVariable(name = "id", required = true) @Min(value = 1L) Long id) {
		return ResponseEntity.ok(bookService.getBookById(id));
	}

	@GetMapping("books/name")
	public ResponseEntity<Page<Book>> getBooksByName(@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByName(name.trim(), page, size));
	}

	@GetMapping("books/category")
	public ResponseEntity<Page<Book>> getBooksByCategory(@RequestParam(name = "category", required = true) String category,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByCategory(category.trim(), page, size));
	}

	@GetMapping("books/price")
	public ResponseEntity<Page<Book>> getBooksByPrice(
			@RequestParam(name = "from", required = false, defaultValue = Const.DEFAULT_MIN_PRICE) @DecimalMin(value = "0.0") Double fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = Const.DEFAULT_MAX_PRICE) @DecimalMin(value = "0.1") Double toPrice,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByPrice(fromPrice, toPrice, page, size));
	}

	@GetMapping("/books/rating")
	public ResponseEntity<Page<Book>> getBooksByRating(
			@RequestParam(name = "rating", required = false, defaultValue = "0") 
			@Min(value = Const.MIN_STAR_NUMBER) @Max(value = Const.MAX_STAR_NUMBER) Integer rating,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByRating(rating, page, size));
	}

	@GetMapping("/books/best_selling")
	public ResponseEntity<Page<Book>> getBooksByBestSelling(
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByBestSelling(page, size));
	}

	@GetMapping("/books/filter")
	public ResponseEntity<Page<Book>> getBooksByFilter(
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "category", required = false, defaultValue = "") String category,
			@RequestParam(name = "from", required = false, defaultValue = Const.DEFAULT_MIN_PRICE) @DecimalMin(value = "0.0") Double fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = Const.DEFAULT_MAX_PRICE) @DecimalMin(value = "0.1") Double toPrice,
			@RequestParam(name = "rating", required = false, defaultValue = "0") 
			@Min(value = Const.MIN_STAR_NUMBER) @Max(value = Const.MAX_STAR_NUMBER) Integer rating,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(bookService.getBooksByFilter(name.trim(), category.trim(), fromPrice, toPrice, rating, page, size));
	}

	@GetMapping("/books/cart")
	public ResponseEntity<List<Book>> getBooksInCart(@RequestParam(name = "ids", required = true) Long... ids) {
		Set<Long> idList = new HashSet<>(Arrays.asList(ids));
		return ResponseEntity.ok(bookService.getBooksInCart(idList));
	}

	@PostMapping("/manage/books")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Book> addBook(@Valid @RequestBody NewBook newBook) {
		return ResponseEntity.ok(bookService.addBook(newBook));
	}

	@PutMapping("/manage/books/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Book> updateBook(@Valid @RequestBody NewBook updateBook,
			@PathVariable(name = "id", required = true) @Min(value = 1L) Long id) {
		return ResponseEntity.ok(bookService.updateBook(updateBook, id));
	}

	@PutMapping("/manage/books/{id}/status")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Book> updateStatus(@Valid @RequestBody UpdateBookStatus bookStatus,
			@PathVariable(name = "id", required = true) @Min(value = 1L) Long id) {
		return ResponseEntity.ok(bookService.updateStatus(id, bookStatus.getStopSelling()));
	}

	@DeleteMapping("/manage/books/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<String> deleteBook(@PathVariable(name = "id", required = true) @Min(value = 1L) Long id) {
		bookService.deleteBook(id);
		return ResponseEntity.ok("Delete book id: " + id + " successfully");
	}

}
