package com.uet.book_a_book.dto.order;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewOrder {
	@NotBlank(message = "address field is mandatory")
	private String address;
	
	@NotEmpty(message = "orderdetails field is mandatory")
	@Valid
	private List<NewOrderdetail> orderdetails;
}
