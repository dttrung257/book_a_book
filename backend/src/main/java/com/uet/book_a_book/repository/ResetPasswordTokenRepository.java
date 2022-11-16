package com.uet.book_a_book.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.entity.ResetPasswordToken;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, UUID> {
	@Query("SELECT r FROM ResetPasswordToken r INNER JOIN AppUser u ON r.user.id = u.id WHERE u.email = :email")
	Optional<ResetPasswordToken> findByEmail(@Param("email") String email);
}
