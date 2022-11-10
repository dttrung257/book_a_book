package com.uet.book_a_book.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.order.NewOrder;
import com.uet.book_a_book.dto.order.OrderAddedByAdmin;
import com.uet.book_a_book.dto.order.OrderDTO;
import com.uet.book_a_book.dto.order.OrderdetailDTO;
import com.uet.book_a_book.entity.Order;

public interface OrderService {
	Order addOrder(NewOrder newOrder);
	Order addOrderFromAdmin(OrderAddedByAdmin newOrder);
	Page<OrderDTO> fetchUserOrder(Integer page, Integer size);
	Page<OrderdetailDTO> fetchOrderdetails(UUID orderId, Integer page, Integer size);
	void cancelOrder(UUID orderId);
	Order updateStatus(UUID orderId, String status);
	Page<OrderDTO> fetchAllOrders(Integer page, Integer size);
	Page<OrderDTO> fetchOrdersByPrice(Double fromPrice, Double toPrice, Integer page, Integer size);
	Page<OrderDTO> fetchOrdersByDate(Date orderDate, Integer page, Integer size);
	Page<OrderDTO> fetchOrdersByUser(String email, Integer page, Integer size);
}
