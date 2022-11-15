package com.uet.book_a_book.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.order.NewOrder;
import com.uet.book_a_book.dto.order.AdmOrder;
import com.uet.book_a_book.dto.order.OrderDTO;
import com.uet.book_a_book.dto.order.OrderdetailDTO;
import com.uet.book_a_book.entity.Order;

public interface OrderService {
	Order addOrder(NewOrder newOrder);
	Order addOrderByAdmin(AdmOrder newOrder);
	Page<OrderDTO> getUserOrders(Integer page, Integer size);
	Page<OrderdetailDTO> getOrderdetails(UUID orderId, Integer page, Integer size);
	void cancelOrder(UUID orderId);
	Order updateStatus(UUID orderId, String status);
	OrderDTO getOrderById(UUID id);
	Page<OrderDTO> getAllOrders(Integer page, Integer size);
	Page<OrderDTO> getOrdersByPrice(Double fromPrice, Double toPrice, Integer page, Integer size);
	Page<OrderDTO> getOrdersByDate(Date orderDate, Integer page, Integer size);
	Page<OrderDTO> getOrdersByEmail(String email, Integer page, Integer size);
}
