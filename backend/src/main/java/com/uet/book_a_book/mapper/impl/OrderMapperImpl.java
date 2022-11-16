package com.uet.book_a_book.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uet.book_a_book.dto.order.OrderDTO;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Order;
import com.uet.book_a_book.mapper.OrderMapper;
import com.uet.book_a_book.repository.OrderRepository;
import com.uet.book_a_book.repository.OrderdetailRepository;

@Component
public class OrderMapperImpl implements OrderMapper {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderdetailRepository orderdetailRepository;

	@Override
	public OrderDTO mapToOrderDTO(Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setId(order.getId());
		orderDTO.setOrderDate(order.getOrderDate());
		orderDTO.setAddress(order.getAddress());
		orderDTO.setStatus(order.getStatus());
		AppUser user = orderRepository.findUserByOrderId(order.getId()).orElse(null);
		if (user == null) {
			orderDTO.setEmail(null);
			orderDTO.setUserId(null);
		} else {
			orderDTO.setUserId(user.getId());
			orderDTO.setEmail(user.getEmail());
		}
		orderDTO.setQuantity(orderdetailRepository.countTotalQuantity(order.getId()));
		orderDTO.setTotal(orderdetailRepository.calculateTotalPrice(order.getId()));
		return orderDTO;
	}

}
