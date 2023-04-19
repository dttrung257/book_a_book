package com.uet.book_a_book.dtos.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserStatus {
	@NotBlank(message = "status field is mandatory")
	private String status;
	
	@NotNull(message = "state field is mandatory")
	private Boolean state;
}
