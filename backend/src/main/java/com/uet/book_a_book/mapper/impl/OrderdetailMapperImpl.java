package com.uet.book_a_book.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uet.book_a_book.dto.order.OrderdetailDTO;
import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.entity.Orderdetail;
import com.uet.book_a_book.mapper.OrderdetailMapper;
import com.uet.book_a_book.repository.OrderdetailRepository;

@Component
public class OrderdetailMapperImpl implements OrderdetailMapper {
	@Autowired
	private OrderdetailRepository orderdetailRepository;

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
