package com.uet.book_a_book.mapper;

import com.uet.book_a_book.dto.order.OrderdetailDTO;
import com.uet.book_a_book.entity.Orderdetail;

public interface OrderdetailMapper {
	OrderdetailDTO mapToOrderdetailDTO(Orderdetail orderdetail);
}
