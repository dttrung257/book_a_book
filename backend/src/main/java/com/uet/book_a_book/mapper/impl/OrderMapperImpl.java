package com.uet.book_a_book.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.uet.book_a_book.dtos.order.OrderDTO;
import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.models.Order;
import com.uet.book_a_book.mapper.OrderMapper;
import com.uet.book_a_book.repositories.OrderRepository;
import com.uet.book_a_book.repositories.OrderdetailRepository;

@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {
	private final OrderRepository orderRepository;
	private final OrderdetailRepository orderdetailRepository;

	@Override
	public OrderDTO mapToOrderDTO(Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setId(order.getId());
		orderDTO.setOrderDate(order.getOrderDate());
		orderDTO.setAddress(order.getAddress());
		orderDTO.setPhoneNumber(order.getPhoneNumber());
		orderDTO.setStatus(order.getStatus());
		AppUser user = orderRepository.findUserByOrderId(order.getId()).orElse(null);
		if (user == null) {
			orderDTO.setEmail(null);
			orderDTO.setUserId(null);
			orderDTO.setFullName(null);
		} else {
			orderDTO.setUserId(user.getId());
			orderDTO.setEmail(user.getEmail());
			orderDTO.setFullName(user.getFirstName() + " " + user.getLastName());
		}
		orderDTO.setQuantity(orderdetailRepository.countTotalQuantity(order.getId()));
		orderDTO.setTotal(Math.ceil(orderdetailRepository.calculateTotalPrice(order.getId()) * 100) / 100);
		return orderDTO;
	}

}
