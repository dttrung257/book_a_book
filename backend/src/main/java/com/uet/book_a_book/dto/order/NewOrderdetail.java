package com.uet.book_a_book.dto.order;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderdetail {
	@NotNull(message = "bookId field is mandatory")
	@Min(value = 1L, message = "bookId field must be in integer format and greater than or equal to 1")
	private Long bookId;
	
	@NotNull(message = "quantity field is mandatory")
	@Min(value = 1L, message = "quantity field must be in integer format greater than or equal to 1")
	private Long quantity;
}
