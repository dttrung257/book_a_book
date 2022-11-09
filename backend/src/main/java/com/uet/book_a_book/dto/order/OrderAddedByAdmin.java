package com.uet.book_a_book.dto.order;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddedByAdmin {
	@NotEmpty(message = "Orderdetails field cannot be empty")
	@Valid
	private List<NewOrderdetail> orderdetails;
}
