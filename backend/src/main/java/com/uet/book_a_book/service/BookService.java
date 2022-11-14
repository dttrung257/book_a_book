package com.uet.book_a_book.service;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.book.BookDTO;
import com.uet.book_a_book.entity.Book;

public interface BookService {
	Page<Book> getAllBooks(Integer page, Integer size);
	Book getBookById(Long id);
	Page<Book> getBooksByName(String name, Integer page, Integer size);
	Page<Book> getBooksByCategory(String category, Integer page, Integer size);
	Page<Book> getBooksByPrice(Double fromPrice, Double toPrice, Integer page, Integer size);
	Page<Book> getBooksByRating(Integer rating, Integer page, Integer size);
	Page<Book> getBooksByBestSelling(Integer page, Integer size);
	Book addBook(BookDTO newBook);
	Book updateBook(BookDTO updateBook, Long id);
	Book updateStatus(Long id, Boolean stopSelling);
	void deleteBook(Long id);
}
