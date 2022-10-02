package com.uet.book_a_book.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uet.book_a_book.model.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {

}
