package com.uet.book_a_book.dtos.book;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookStatus {
	@NotNull(message = "stopSelling field is mandatory")
	private Boolean stopSelling;
}
