package com.uet.book_a_book.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.models.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
	@Query("SELECT o FROM Order o WHERE o.id = :id")
	Optional<Order> findById(@Param("id") UUID id);
	
	@Query("SELECT o FROM Order o WHERE o.user.id  = :userId")
	List<Order> findOrdersByUserId(@Param("userId") UUID userId);
	
	@Query("SELECT u FROM Order o INNER JOIN AppUser u ON o.user.id = u.id WHERE o.id = :orderId")
	Optional<AppUser> findUserByOrderId(@Param("orderId") UUID orderId);

	@Query("SELECT COUNT(o) FROM Order o " +
			"WHERE (:year IS NULL OR YEAR(o.orderDate) = :year) " +
			"AND (:month IS NULL OR MONTH(o.orderDate) = :month) " +
			"AND o.status = :status")
	Optional<Integer> getNumberOfOrdersByStatus(@Param(value = "year") Integer year,
												@Param(value = "month") Integer month,
												@Param(value = "status") String status);
}
