package com.uet.book_a_book.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.book.NewBook;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.exception.book.BookAlreadyExistsException;
import com.uet.book_a_book.exception.book.NotFoundBookException;
import com.uet.book_a_book.repository.BookRepository;
import com.uet.book_a_book.service.BookService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
	@Autowired
	private BookRepository bookRepository;

	/** Get all books. **/
	@Override
	public Page<Book> getAllBooks(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return bookRepository.findAll(pageable);
	}

	/** Get book by id. **/
	@Override
	public Book getBookById(Long id) {
		Book book = bookRepository.findById(id).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book id: " + id);
		}
		return book;
	}

	/** Get books by name. **/
	@Override
	public Page<Book> getBooksByName(String name, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		if (name.equals("")) {
			return bookRepository.findAll(pageable);
		}
		return bookRepository.findByName(name.trim(), pageable);
	}

	/** Get books by category. **/
	@Override
	public Page<Book> getBooksByCategory(String category, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		if (category.equals("")) {
			return bookRepository.findAll(pageable);
		}
		return bookRepository.findByCategory(category.trim().toUpperCase(), pageable);
	}

	/** Get books in price range. **/
	@Override
	public Page<Book> getBooksByPrice(Double fromPrice, Double toPrice, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		return bookRepository.findByPrice(fromPrice, toPrice, pageable);
	}

	/** Get books by rating. **/
	@Override
	public Page<Book> getBooksByRating(Integer rating, Integer page, Integer size) {
		Sort sort = Sort.by("rating").descending();
		Pageable pageable = PageRequest.of(page, size, sort);
		if (rating == 0) {
			return bookRepository.findByHighestRating(pageable); 
		}
		return bookRepository.findByRating(rating, pageable);
	}

	/** Get best selling books. **/
	@Override
	public Page<Book> getBooksByBestSelling(Integer page, Integer size) {
		Sort sort = Sort.by("quantitySold").descending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return bookRepository.findByBestSelling(pageable);
	}

	/** Get books by many criteria. **/
	@Override
	public Page<Book> getBooksByFilter(String name, String category, Double fromPrice, Double toPrice, Integer rating,
			Integer page, Integer size) {
		List<Book> books = bookRepository.findAll();
		if (!(name.trim().equals("") || name == null)) {
			books = books.stream().filter(book -> book.getName().toLowerCase().contains(name.toLowerCase()))
					.collect(Collectors.toList());
		}
		if (!(category.trim().equals("") || category == null)) {
			books = books.stream().filter(book -> book.getCategory().equalsIgnoreCase(category))
					.collect(Collectors.toList());
		}
		if (rating != 0) {
			books = books.stream().
					filter(book -> (book.getRating() != null && book.getRating().intValue() >= rating))
					.sorted(Comparator.comparing(Book::getRating).reversed())
					.collect(Collectors.toList());
		}
		books = books.stream()
				.filter(book -> (book.getSellingPrice() >= fromPrice && book.getSellingPrice() <= toPrice))
				.collect(Collectors.toList());
		Pageable pageable = PageRequest.of(page, size);
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), books.size());
		if (start <= books.size()) {
			return new PageImpl<>(books.subList(start, end), pageable, books.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, books.size());
	}

	/** Get books in cart. **/
	@Override
	public List<Book> getBooksInCart(Set<Long> ids) {
		List<Book> books = bookRepository.findAll();
		return books.stream().filter(b -> ids.contains(b.getId())).collect(Collectors.toList());
	}

	/** Add a new book. **/
	@Override
	public Book addBook(NewBook newBook) {
		Book checkBook = bookRepository.findByNameAndAuthor(newBook.getName().trim(), newBook.getAuthor().trim())
				.orElse(null);
		if (checkBook != null) {
			throw new BookAlreadyExistsException("The book named " + newBook.getName().trim() + " already exists");
		}
		Book book = new Book();
		book.setName(newBook.getName().trim());
		book.setAuthor(newBook.getAuthor().trim());
		book.setCategory(newBook.getCategory().toUpperCase());
		book.setIsbn(newBook.getIsbn());
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
		book.setDescription(newBook.getDescription());
		book.setRating(null);
		book.setOrderdetails(new ArrayList<>());
		book.setComments(new ArrayList<>());
		bookRepository.save(book);
		log.info("Admin id: {} added new book id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
				book.getId());
		return book;
	}

	/** Update a book. **/
	@Override
	public Book updateBook(NewBook updateBook, Long id) {
		Book checkBook = bookRepository.findByNameAndAuthor(updateBook.getName().trim(), updateBook.getAuthor().trim())
				.orElse(null);
		if (checkBook != null && checkBook.getId() != id) {
			throw new BookAlreadyExistsException(
					"The book named " + updateBook.getName() + " already exists with id: " + checkBook.getId());
		}
		Book book = bookRepository.findById(id).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book id: " + id);
		}
		book.setName(updateBook.getName().trim());
		book.setAuthor(updateBook.getAuthor().trim());
		book.setCategory(updateBook.getCategory().toUpperCase());
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
		book.setDescription(updateBook.getDescription());
		bookRepository.save(book);
		log.info("Admin id: {} updated book id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
				book.getId());
		return book;
	}

	/** Update status of book (stop selling or keep selling). **/
	@Override
	public Book updateStatus(Long id, Boolean stopSelling) {
		Book book = bookRepository.findById(id).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book id: " + id);
		}
		if (book.isStopSelling() == stopSelling) {
			return book;
		}
		book.setStopSelling(stopSelling);
		bookRepository.save(book);
		if (stopSelling == true) {
			log.info("Admin id: {} changed the status of book id: {} to stop selling.",
					((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
					book.getId());
		} else {
			log.info("Admin id: {} changed the status of book id: {} to keep selling.",
					((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
					book.getId());
		}
		return book;
	}

	/** Delete a book. **/
	@Override
	public void deleteBook(Long id) {
		Book book = bookRepository.findById(id).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book id: " + id);
		}
		log.info("Admin id: {} deleted book id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
				book.getId());
		bookRepository.delete(book);
	}
}
