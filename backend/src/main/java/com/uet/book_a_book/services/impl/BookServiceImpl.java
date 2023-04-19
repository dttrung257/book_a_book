package com.uet.book_a_book.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dtos.book.NewBook;
import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.models.Book;
import com.uet.book_a_book.models.constant.OrderStatus;
import com.uet.book_a_book.exception.book.BookAlreadyExistsException;
import com.uet.book_a_book.exception.book.CannotDeleteBookException;
import com.uet.book_a_book.exception.book.NotFoundBookException;
import com.uet.book_a_book.repositories.BookRepository;
import com.uet.book_a_book.repositories.OrderdetailRepository;
import com.uet.book_a_book.services.BookService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
	private final BookRepository bookRepository;
	private final OrderdetailRepository orderdetailRepository;

	/** Get book by id. **/
	@Override
	public Book getBookById(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new NotFoundBookException("Not found book id: " + id));
	}

	/** Get books. **/
	@Override
	@Transactional(readOnly = true)
	public Page<Book> getBooks(String name, String category, Double fromPrice, Double toPrice, Integer rating,
			Boolean bestSelling, Integer page, Integer size) {
		final Pageable pageable = bestSelling ? PageRequest.of(page, size, Sort.by("quantitySold").descending())
				: PageRequest.of(page, size);
		final Page<Book> books = bookRepository.findBooksByFilters(name, category,
				rating, fromPrice, toPrice, pageable);
		final List<Book> listBooks = books.getContent()
				.stream()
				.toList();
		return new PageImpl<>(listBooks, pageable, books.getTotalElements());
	}

	/** Get books in cart. **/
	@Override
	public List<Book> getBooksInCart(Set<Long> ids) {
		List<Book> books = bookRepository.findAll();
		return books.stream().filter(b -> ids.contains(b.getId()))
				// .filter(b -> (!b.isStopSelling()))
				.collect(Collectors.toList());
	}

	/** Add a new book. **/
	@Override
	@Transactional
	public Book addBook(NewBook newBook) {
		Optional<Book> checkBook = bookRepository.findByNameAndAuthor(newBook.getName().trim(),
				newBook.getAuthor().trim());
		if (checkBook.isPresent()) {
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
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
				book.getId());
		return book;
	}

	/** Update a book. **/
	@Override
	@Transactional
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
		if (updateBook.getIsbn() != null) {
			book.setIsbn(updateBook.getIsbn().trim());
		}
		if (updateBook.getPublisher() != null) {
			book.setPublisher(updateBook.getPublisher().trim());
		}
		book.setBuyPrice(updateBook.getBuyPrice());
		book.setSellingPrice(updateBook.getSellingPrice());
		book.setNumberOfPages(updateBook.getNumberOfPages());
		book.setWidth(updateBook.getWidth());
		book.setHeight(updateBook.getHeight());
		book.setImage(updateBook.getImage());
		long diff = book.getQuantityInStock() - book.getAvailableQuantity();
		if (updateBook.getQuantityInStock() - diff <= 0) {
			book.setAvailableQuantity(0L);
		} else {
			book.setAvailableQuantity(updateBook.getQuantityInStock() - diff);
		}
		book.setQuantityInStock(updateBook.getQuantityInStock());
		if (updateBook.getDescription() != null) {
			book.setDescription(updateBook.getDescription().trim());
		}
		bookRepository.save(book);
		log.info("Admin id: {} updated book id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
				book.getId());
		return book;
	}

	/** Update status of book (stop selling or keep selling). **/
	@Override
	@Transactional
	public Book updateStatus(Long id, Boolean stopSelling) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new NotFoundBookException("Not found book id: " + id));
		if (book.isStopSelling() == stopSelling) {
			return book;
		}
		book.setStopSelling(stopSelling);
		if (stopSelling) {
			log.info("Admin id: {} changed the status of book id: {} to stop selling.",
					((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
					book.getId());
		} else {
			log.info("Admin id: {} changed the status of book id: {} to keep selling.",
					((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
					book.getId());
		}
		return bookRepository.save(book);
	}

	/** Delete a book. **/
	@Override
	@Transactional
	public void deleteBook(Long id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new NotFoundBookException("Not found book id: " + id));
		if (orderdetailRepository.calculateTotalOrderOfBook(id, OrderStatus.STATUS_CANCELED) > 0) {
			throw new CannotDeleteBookException(
					"Book id: " + id + " cannot be deleted while there is an order pending or shipping or success");
		}
		log.info("Admin id: {} deleted book id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(),
				book.getId());
		bookRepository.deleteById(id);
	}
}
