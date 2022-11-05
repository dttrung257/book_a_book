package com.uet.book_a_book.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	@Query("SELECT b FROM Book b")
	Page<Book> findAll(Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE b.id = :id")
	Optional<Book> findById(@Param("id") Long id);
	
	@Query("SELECT b FROM Book b WHERE b.name like :name%")
	Page<Book> findByName(@Param("name") String name, Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE b.category = :category")
	Page<Book> findByCategory(@Param("category") String category, Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE b.sellingPrice BETWEEN :fromPrice AND :toPrice")
	Page<Book> findByPrice(@Param("fromPrice") Double fromPrice, @Param("toPrice") Double toPrice, Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE FLOOR(b.rating) = :rating")
	Page<Book> findByRating(@Param("rating") Integer rating, Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE b.rating IS NOT NULL")
	Page<Book> findByHighestRating(Pageable pageable);
	
	@Query("SELECT b FROM Book b WHERE b.name = :name AND b.author = :author")
	Optional<Book> findByNameAndAuthor(@Param("name") String name, @Param("author") String author);
	
	@Query("SELECT COUNT(c) FROM Book b INNER JOIN Comment c ON b.id = c.book.id WHERE b.id = :id")
	Long countComment(@Param("id") Long id);
}
