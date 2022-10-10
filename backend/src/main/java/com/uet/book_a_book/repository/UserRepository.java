package com.uet.book_a_book.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.domain.AppUser;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
	@Query("SELECT u FROM AppUser u JOIN FETCH u.roles")
	List<AppUser> findAllUsers();
	
	@Query("SELECT u FROM AppUser u JOIN FETCH u.roles LEFT JOIN FETCH u.comments WHERE u.email = :email")
	Optional<AppUser> findByUserEmail(@Param("email") String email);
	
	@Query("SELECT u FROM AppUser u JOIN FETCH u.roles WHERE u.email = :email AND u.emailVerificationCode = :code ")
	Optional<AppUser> findUserByEmailAndVerificationCode(@Param("email") String email, @Param("code") String code);
	
	boolean existsByEmail(String email);
	
}
