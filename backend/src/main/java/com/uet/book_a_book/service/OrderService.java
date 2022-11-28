package com.uet.book_a_book.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.order.AdmOrder;
import com.uet.book_a_book.dto.order.NewOrder;
import com.uet.book_a_book.dto.order.OrderDTO;
import com.uet.book_a_book.dto.order.OrderdetailDTO;

public interface OrderService {
	// For users
	Page<OrderDTO> getUserOrders(Integer page, Integer size);

	Page<OrderdetailDTO> getOrderdetails(UUID orderId, Integer page, Integer size);
	
	// For admins
	Page<OrderDTO> getOrdersByUserId(UUID id, Integer page, Integer size);

	OrderDTO getOrderById(UUID userId);

	Page<OrderDTO> getAllOrders(Integer page, Integer size);

	Page<OrderDTO> getOrdersByPrice(Double fromPrice, Double toPrice, Integer page, Integer size);

	Page<OrderDTO> getOrdersByDate(Date orderDate, Integer page, Integer size);

	Page<OrderDTO> getOrdersByEmail(String email, Integer page, Integer size);
	
	Page<OrderDTO> getOrdersByFilter(String name, String status, Double fromPrice, Double toPrice, Date orderDate, Integer page, Integer size);
	
	// For users
	OrderDTO addOrder(NewOrder newOrder);

	OrderDTO addOrderByAdmin(AdmOrder newOrder);

	void cancelOrder(UUID orderId);
	
	// For admins
	OrderDTO updateStatus(UUID orderId, String status);

	void deleteOrder(UUID id);
}
