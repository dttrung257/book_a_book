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
	OrderDTO getOrderById(UUID id);

	Page<OrderDTO> getOrders(String userId, String name, String status, Double fromPrice, Double toPrice,
			Date orderDate, Integer page, Integer size);

	// For users
	OrderDTO addOrder(NewOrder newOrder);

	OrderDTO addOrderByAdmin(AdmOrder newOrder);

	void cancelOrder(UUID orderId);

	// For admins
	OrderDTO updateStatus(UUID orderId, String status);

	void deleteOrder(UUID id);
}
