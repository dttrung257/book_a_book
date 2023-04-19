package com.uet.book_a_book.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.RequiredArgsConstructor;
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

import com.uet.book_a_book.dtos.book.NewBook;
import com.uet.book_a_book.dtos.book.UpdateBookStatus;
import com.uet.book_a_book.models.Book;
import com.uet.book_a_book.models.constant.Const;
import com.uet.book_a_book.services.BookService;

@RestController
@RequestMapping("/api")
@Validated
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(name = "id", required = true) @Min(value = 1L) Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/books")
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "category", required = false, defaultValue = "") String category,
            @RequestParam(name = "from", required = false, defaultValue = Const.DEFAULT_MIN_PRICE) @DecimalMin(value = "0.0") Double fromPrice,
            @RequestParam(name = "to", required = false, defaultValue = Const.DEFAULT_MAX_PRICE) @DecimalMin(value = "0.1") Double toPrice,
            @RequestParam(name = "rating", required = false, defaultValue = "0")
            @Min(value = Const.MIN_STAR_NUMBER) @Max(value = Const.MAX_STAR_NUMBER) Integer rating,
            @RequestParam(name = "best_selling", required = false, defaultValue = "false") Boolean bestSelling,
            @RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
        return ResponseEntity.ok(bookService.getBooks(name.trim(), category.trim(), fromPrice, toPrice, rating, bestSelling, page, size));
    }

    @GetMapping("/books/cart")
    public ResponseEntity<List<Book>> getBooksInCart(@RequestParam(name = "ids", required = true) Long... ids) {
        final Set<Long> idList = new HashSet<>(Arrays.asList(ids));
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