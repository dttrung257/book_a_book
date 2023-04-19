package com.uet.book_a_book.mapper;

import com.uet.book_a_book.dtos.order.OrderDTO;
import com.uet.book_a_book.models.Order;

public interface OrderMapper {
	OrderDTO mapToOrderDTO(Order order);
}
