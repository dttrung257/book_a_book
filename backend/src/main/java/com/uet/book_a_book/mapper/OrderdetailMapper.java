package com.uet.book_a_book.mapper;

import com.uet.book_a_book.dtos.order.OrderdetailDTO;
import com.uet.book_a_book.models.Orderdetail;

public interface OrderdetailMapper {
	OrderdetailDTO mapToOrderdetailDTO(Orderdetail orderdetail);
}
