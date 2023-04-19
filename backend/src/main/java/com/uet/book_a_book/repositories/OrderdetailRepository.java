package com.uet.book_a_book.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.models.Book;
import com.uet.book_a_book.models.Orderdetail;

public interface OrderdetailRepository extends JpaRepository<Orderdetail, UUID> {
	@Query("SELECT od FROM Orderdetail od WHERE od.order.id = :orderId")
	List<Orderdetail> findByOrderId(@Param("orderId") UUID orderId);
	
	@Query("SELECT b FROM Orderdetail od INNER JOIN Book b ON od.book.id = b.id WHERE od.id = :orderdetailId")
	Optional<Book> findBookByOrderdetailId(@Param("orderdetailId") UUID orderdetailId);
	
	@Query("SELECT SUM(od.quantityOrdered) FROM Orderdetail od WHERE od.order.id = :orderId")
	Long countTotalQuantity(@Param("orderId") UUID orderId);
	
	@Query("SELECT SUM(od.quantityOrdered * od.priceEach) FROM Orderdetail od WHERE od.order.id = :orderId")
	Double calculateTotalPrice(@Param("orderId") UUID orderId);
	
	@Query("SELECT COUNT(o) FROM Orderdetail od INNER JOIN Order o ON od.order.id = o.id WHERE od.book.id = :bookId AND o.status != :status")
	Long calculateTotalOrderOfBook(@Param("bookId") Long bookId, @Param("status") String status);

	@Query("SELECT SUM(od.quantityOrdered * od.priceEach) FROM Orderdetail od " +
			"INNER JOIN Order o ON od.order.id = o.id " +
			"WHERE (:year IS NULL OR YEAR(o.orderDate) = :year) " +
			"AND (:month IS NULL OR MONTH(o.orderDate) = :month) " +
			"AND o.status = 'SUCCESS'")
	Optional<Double> getRevenue(@Param(value = "year") Integer year, @Param(value = "month") Integer month);

	@Query("SELECT SUM(od.quantityOrdered) FROM Orderdetail od " +
			"INNER JOIN Order o ON od.order.id = o.id " +
			"WHERE (:year IS NULL OR YEAR(o.orderDate) = :year) " +
			"AND (:month IS NULL OR MONTH(o.orderDate) = :month) " +
			"AND o.status = 'SUCCESS'")
	Optional<Long> getBooksSold(@Param(value = "year") Integer year, @Param(value = "month") Integer month);
}
