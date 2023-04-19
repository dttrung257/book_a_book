package com.uet.book_a_book.mapper.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.uet.book_a_book.dtos.order.OrderdetailDTO;
import com.uet.book_a_book.models.Book;
import com.uet.book_a_book.models.Orderdetail;
import com.uet.book_a_book.mapper.OrderdetailMapper;
import com.uet.book_a_book.repositories.OrderdetailRepository;

@Component
@RequiredArgsConstructor
public class OrderdetailMapperImpl implements OrderdetailMapper {
	private final OrderdetailRepository orderdetailRepository;

	@Override
	public OrderdetailDTO mapToOrderdetailDTO(Orderdetail orderdetail) {
		OrderdetailDTO orderdetailDTO = new OrderdetailDTO();
		orderdetailDTO.setId(orderdetail.getId());
		orderdetailDTO.setPriceEach(orderdetail.getPriceEach());
		orderdetailDTO.setQuantityOrdered(orderdetail.getQuantityOrdered());
		Book book = orderdetailRepository.findBookByOrderdetailId(orderdetail.getId()).orElse(null);
		if (book == null) {
			orderdetailDTO.setImage(null);
			orderdetailDTO.setBookName(null);
		} else {
			orderdetailDTO.setImage(book.getImage());
			orderdetailDTO.setBookName(book.getName());
		}
		return orderdetailDTO;
	}

}
