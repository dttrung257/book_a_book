package com.uet.book_a_book.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
	@Query("SELECT o FROM Order o WHERE o.id = :id")
	Optional<Order> findById(@Param("id") UUID id);
	
	@Query("SELECT o FROM Order o WHERE o.user.id  = :userId")
	Page<Order> fetchUserOrder(@Param("userId") UUID userId, Pageable pageable);
	
	@Query("SELECT u FROM Order o INNER JOIN AppUser u ON o.user.id = u.id WHERE o.id = :orderId")
	Optional<AppUser> findUserByOrderId(@Param("orderId") UUID orderId);
	
	
}
