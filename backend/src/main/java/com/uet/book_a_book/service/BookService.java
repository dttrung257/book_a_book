package com.uet.book_a_book.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.book.NewBook;
import com.uet.book_a_book.entity.Book;

public interface BookService {
	// Get books
	Book getBookById(Long id);

	// Get book by id
	Page<Book> getBooks(String name, String category, Double fromPrice, Double toPrice, Integer rating, Boolean bestSelling,
			Integer page, Integer size);
	
	// Get books in shopping cart
	List<Book> getBooksInCart(Set<Long> ids);
	
	// Adm add new book
	Book addBook(NewBook newBook);

	// Adm update book
	Book updateBook(NewBook updateBook, Long id);

	// Adm update book status
	Book updateStatus(Long id, Boolean stopSelling);

	// Adm delete book
	void deleteBook(Long id);
}
