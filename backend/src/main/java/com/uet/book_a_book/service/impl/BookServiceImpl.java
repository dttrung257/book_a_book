package com.uet.book_a_book.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.book.NewBook;
import com.uet.book_a_book.dto.book.UpdateBook;
import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.exception.book.BookAlreadyExistsException;
import com.uet.book_a_book.exception.book.NotFoundBookException;
import com.uet.book_a_book.repository.BookRepository;
import com.uet.book_a_book.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public Page<Book> findAll(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return bookRepository.findAll(pageable);
	}

	@Override
	public Book findById(Long id) {
		Book book = bookRepository.findById(id).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book with id: " + id);
		}
		return book;
	}

	@Override
	public Page<Book> findByName(String name, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		if (name.equals("")) {
			return bookRepository.findAll(pageable);
		}
		return bookRepository.findByName(name.trim(), pageable);
	}

	@Override
	public Page<Book> findByCategory(String category, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		if (category.equals("")) {
			return bookRepository.findAll(pageable);
		}
		return bookRepository.findByCategory(category.trim().toUpperCase(), pageable);
	}

	@Override
	public Page<Book> findByPrice(Double fromPrice, Double toPrice, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return bookRepository.findByPrice(fromPrice, toPrice, pageable);
	}

	@Override
	public Page<Book> findByRating(Integer rating, Integer page, Integer size) {
		if (rating == 0) {
			Sort sort = Sort.by("rating").descending();
			Pageable pageable = PageRequest.of(page, size, sort);
			return bookRepository.findByHighestRating(pageable);
		}
		Pageable pageable = PageRequest.of(page, size);
		return bookRepository.findByRating(rating, pageable);
	}

	@Override
	public Book addBook(NewBook newBook) {
		Book checkBook = bookRepository.findByNameAndAuthor(newBook.getName(), newBook.getAuthor()).orElse(null);
		if (checkBook != null) {
			throw new BookAlreadyExistsException("The book named " + newBook.getName() + " already exists");
		}
		Book book = new Book();
		book.setName(newBook.getName());
		book.setAuthor(newBook.getAuthor());
		book.setCategory(newBook.getCategory());
		book.setIsbn(newBook.getIsbn());
		book.setWidth(newBook.getWidth());
		book.setPublisher(newBook.getPublisher());
		book.setBuyPrice(newBook.getBuyPrice());
		book.setSellingPrice(newBook.getSellingPrice());
		book.setNumberOfPages(newBook.getNumberOfPages());
		book.setWidth(newBook.getWidth());
		book.setHeight(newBook.getHeight());
		book.setImage(newBook.getImage());
		book.setQuantityInStock(newBook.getQuantityInStock());
		book.setAvailableQuantity(newBook.getQuantityInStock());
		book.setQuantitySold(0L);
		book.setStopSelling(false);
		book.setDescription(newBook.getDesception());
		book.setRating(null);
		book.setOrderdetail(null);
		book.setComments(new ArrayList<>());
		bookRepository.save(book);
		return book;
	}

	@Override
	public Book updateBook(UpdateBook updateBook) {
		Book book = bookRepository.findById(updateBook.getId()).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book with id: " + updateBook.getId());
		}
		book.setName(updateBook.getName());
		book.setAuthor(updateBook.getAuthor());
		book.setCategory(updateBook.getCategory());
		book.setIsbn(updateBook.getIsbn());
		book.setWidth(updateBook.getWidth());
		book.setPublisher(updateBook.getPublisher());
		book.setBuyPrice(updateBook.getBuyPrice());
		book.setSellingPrice(updateBook.getSellingPrice());
		book.setNumberOfPages(updateBook.getNumberOfPages());
		book.setWidth(updateBook.getWidth());
		book.setHeight(updateBook.getHeight());
		book.setImage(updateBook.getImage());
		book.setQuantityInStock(updateBook.getQuantityInStock());
		book.setDescription(updateBook.getDesception());
		bookRepository.save(book);
		return book;
	}

	@Override
	public void deleteBook(Long id) {
		Book book = bookRepository.findById(id).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book with id: " + id);
		}
		bookRepository.delete(book);
	}
	
}
