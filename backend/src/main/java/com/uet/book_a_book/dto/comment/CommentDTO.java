package com.uet.book_a_book.dto.comment;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class CommentDTO {
	@NotNull(message = "bookId field is mandatory")
	@Min(value = 1L, message = "bookId must be greater than or equal to 1")
	private Long bookId;
	
	@NotNull(message = "star field is mandatory")
	@Min(value = 1, message = "star field must be greater than or equal to 1")
	@Max(value = 5, message = "star field must be less than or equal to 5")
	private Integer star;
	
	@NotBlank(message = "content field is mandatory")
	private String content;
}
