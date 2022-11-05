package com.uet.book_a_book.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
	@Query("SELECT c FROM Comment c WHERE c.book.id = :bookId AND c.user.id <> :userId")
	List<Comment> commentByOtherUser(@Param("userId") UUID userId, @Param("bookId") Long bookId);
	
	@Query("SELECT c FROM Comment c WHERE c.user.id = :userId AND c.book.id = :bookId")
	List<Comment> commentByUser(@Param("userId") UUID userId, @Param("bookId") Long bookId);
	
	@Query("SELECT AVG(c.star) FROM Comment c WHERE c.book.id = :bookId")
	Double calculateRateOfBook(@Param("bookId") Long bookId);
}
