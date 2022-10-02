package com.uet.book_a_book.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uet.book_a_book.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
	List<AppUser> findAll();
	Optional<AppUser> findByEmail(String email);
}
