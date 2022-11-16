package com.uet.book_a_book.mapper;

import com.uet.book_a_book.dto.order.OrderDTO;
import com.uet.book_a_book.entity.Order;

public interface OrderMapper {
	OrderDTO mapToOrderDTO(Order order);
}
