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
	@NotNull(message = "Book id field cannot be null")
	@Min(value = 1L, message = "Book id field must be in integer format and greater than or equal to 1")
	private Long bookId;
	
	@NotNull(message = "Quantity field cannot be null")
	@Min(value = 1L, message = "Quantity field must be in double format greater than or equal to 1")
	private Long quantity;
}
