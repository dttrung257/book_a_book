package com.uet.book_a_book.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.models.Book;
import com.uet.book_a_book.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
	@Query("SELECT c FROM Comment c WHERE c.book.id = :bookId")
	List<Comment> findAll(@Param("bookId") Long bookId);
	
	@Query("SELECT c FROM Comment c WHERE c.book.id = :bookId AND c.user.id <> :userId")
	List<Comment> findOtherUserComments(@Param("userId") UUID userId, @Param("bookId") Long bookId);
	
	@Query("SELECT c FROM Comment c WHERE c.user.id = :userId AND c.book.id = :bookId")
	List<Comment> findUserComment(@Param("userId") UUID userId, @Param("bookId") Long bookId);
	
	@Query("SELECT AVG(c.star) FROM Comment c WHERE c.book.id = :bookId")
	Double calculateRateOfBook(@Param("bookId") Long bookId);
	
	@Query("SELECT c FROM Comment c WHERE c.id = :id")
	Optional<Comment> findById(@Param("id") UUID id);
	
	@Query("SELECT b FROM Comment c INNER JOIN Book b ON c.book.id = b.id WHERE c.id = :id")
	Optional<Book> findBookByCommentId(@Param("id") UUID id);
	
	@Query("SELECT u FROM Comment c INNER JOIN AppUser u ON c.user.id = u.id WHERE c.id = :id")
	Optional<AppUser> findUserByCommentId(@Param("id") UUID id);
	
	boolean existsById(UUID commentId);
}
