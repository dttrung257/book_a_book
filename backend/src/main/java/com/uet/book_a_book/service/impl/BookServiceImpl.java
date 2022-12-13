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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.book.NewBook;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.entity.constant.OrderStatus;
import com.uet.book_a_book.exception.book.BookAlreadyExistsException;
import com.uet.book_a_book.exception.book.CannotDeleteBookException;
import com.uet.book_a_book.exception.book.NotFoundBookException;
import com.uet.book_a_book.repository.BookRepository;
import com.uet.book_a_book.repository.OrderdetailRepository;
import com.uet.book_a_book.service.BookService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private OrderdetailRepository orderdetailRepository;

	/** Get book by id. **/
	@Override
	public Book getBookById(Long id) {
		Book book = bookRepository.findById(id).orElse(null);
		if (book == null) {
			throw new NotFoundBookException("Not found book id: " + id);
		}
		return book;
	}

	/** Get books. **/
	@Override
	public Page<Book> getBooks(String name, String category, Double fromPrice, Double toPrice, Integer rating, Boolean bestSelling,
			Integer page, Integer size) {
		List<Book> books = bookRepository.findAll();
		if (!(name.equals("") || name == null)) { // If search by book name
			books = books.stream().filter(book -> book.getName().toLowerCase().contains(name.toLowerCase()))
					.collect(Collectors.toList());
		}
		if (!(category.equals("") || category == null)) { // If search by category
			books = books.stream().filter(book -> book.getCategory().equalsIgnoreCase(category))
					.collect(Collectors.toList());
		}
		if (rating > 0) { // If search by rating
			books = books.stream().
					filter(book -> (book.getRating() != null && book.getRating().intValue() >= rating))
					.sorted(Comparator.comparing(Book::getRating).reversed())
					.collect(Collectors.toList());
		}
		if (bestSelling) { // If search by best selling
			books = books.stream().sorted(Comparator.comparing(Book::getQuantitySold).reversed())
					.collect(Collectors.toList());
		}
		// Price range
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
		return books.stream().filter(b -> ids.contains(b.getId()))
				//.filter(b -> (!b.isStopSelling()))
				.collect(Collectors.toList());
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
		book.setCategory(newBook.getCategory().trim().toUpperCase());
		if (newBook.getIsbn() != null) {
			book.setIsbn(newBook.getIsbn().trim());
		}
		if (newBook.getPublisher() != null) {
			book.setPublisher(newBook.getPublisher().trim());
		}
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
		if (newBook.getDescription() != null) {
			book.setDescription(newBook.getDescription().trim());
		}
		book.setRating(null);
		book.setOrderdetails(new ArrayList<>());
		book.setComments(new ArrayList<>());
		bookRepository.save(book);
		log.info("Admin id: {} added new book id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), book.getId());
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
		book.setCategory(updateBook.getCategory().trim().toUpperCase());
		book.setIsbn(updateBook.getIsbn().trim());
		book.setWidth(updateBook.getWidth());
		book.setPublisher(updateBook.getPublisher().trim());
		book.setBuyPrice(updateBook.getBuyPrice());
		book.setSellingPrice(updateBook.getSellingPrice());
		book.setNumberOfPages(updateBook.getNumberOfPages());
		book.setWidth(updateBook.getWidth());
		book.setHeight(updateBook.getHeight());
		book.setImage(updateBook.getImage());
		book.setQuantityInStock(updateBook.getQuantityInStock());
		book.setDescription(updateBook.getDescription().trim());
		bookRepository.save(book);
		log.info("Admin id: {} updated book id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), book.getId());
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
					((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), book.getId());
		} else {
			log.info("Admin id: {} changed the status of book id: {} to keep selling.",
					((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), book.getId());
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
		if (orderdetailRepository.calculateTotalOrderOfBook(id, OrderStatus.STATUS_CANCELED) > 0) {
			throw new CannotDeleteBookException("Book id: " + id + " cannot be deleted while there is an order pending or shipping or success");
		}
		log.info("Admin id: {} deleted book id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), book.getId());
		bookRepository.delete(book);
	}
}
