package com.uet.book_a_book.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.entity.Orderdetail;

public interface OrderdetailRepository extends JpaRepository<Orderdetail, UUID> {
	@Query("SELECT od FROM Orderdetail od WHERE od.order.id = :orderId")
	List<Orderdetail> findByOrderId(@Param("orderId") UUID orderId);
	
	@Query("SELECT od FROM Orderdetail od WHERE od.order.id = :orderId")
	Page<Orderdetail> fetchByOrderId(@Param("orderId") UUID orderId, Pageable pageable);
	
	@Query("SELECT b FROM Orderdetail od INNER JOIN Book b ON od.book.id = b.id WHERE od.id = :orderdetailId")
	Optional<Book> findBookByOrderdetailId(@Param("orderdetailId") UUID orderdetailId);
	
	@Query("SELECT SUM(od.quantityOrdered) FROM Orderdetail od WHERE od.order.id = :orderId")
	Long countTotalQuantity(@Param("orderId") UUID orderId);
	
	@Query("SELECT SUM(od.priceEach) FROM Orderdetail od WHERE od.order.id = :orderId")
	Double calculateTotalPrice(@Param("orderId") UUID orderId);
}
