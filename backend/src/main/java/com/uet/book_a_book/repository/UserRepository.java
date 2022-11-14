package com.uet.book_a_book.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.entity.AppUser;

@Configuration
public interface UserRepository extends JpaRepository<AppUser, UUID> {
	@Query("SELECT u FROM AppUser u JOIN FETCH u.role WHERE u.email = :email")
	Optional<AppUser> findByUserEmail(@Param("email") String email);
	
	@Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.role WHERE u.id = :id")
	Optional<AppUser> findById(@Param("id") UUID id);
	
	@Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.role"
			+ " WHERE u.email LIKE %:name%"
			+ " OR u.firstName LIKE %:name%"
			+ " OR u.lastName LIKE %:name%"
			+ " OR CONCAT(u.firstName, ' ', u.lastName) LIKE %:name%"
			+ " OR CONCAT(u.lastName, ' ', u.firstName) LIKE %:name%")
	List<AppUser> fetchByName(@Param("name") String name);
	
	@Query("SELECT u FROM AppUser u JOIN FETCH u.role")
	List<AppUser> fetchAllUsers();
	
}
