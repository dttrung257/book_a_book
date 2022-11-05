package com.uet.book_a_book.service;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.book.NewBook;
import com.uet.book_a_book.dto.book.UpdateBook;
import com.uet.book_a_book.entity.Book;

public interface BookService {
	Page<Book> findAll(Integer page, Integer size);
	Book findById(Long id);
	Page<Book> findByName(String name, Integer page, Integer size);
	Page<Book> findByCategory(String category, Integer page, Integer size);
	Page<Book> findByPrice(Double fromPrice, Double toPrice, Integer page, Integer size);
	Page<Book> findByRating(Integer rating, Integer page, Integer size);
	Page<Book> findByBestSelling(Integer page, Integer size);
	Book addBook(NewBook newBook);
	Book updateBook(UpdateBook updateBook);
	void deleteBook(Long id);
}
