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
public class NewComment {
	@NotNull(message = "Book id field cannot be null")
	@Min(value = 1L, message = "Book id field is invalid")
	private Long bookId;
	
	@NotNull(message = "The number of stars cannot be null")
	@Min(value = 1, message = "The number of stars must be in integer format greater than or equal to 1")
	@Max(value = 5, message = "The number of stars must be in integer format lesser than or equal to 5")
	private Integer star;
	
	@NotBlank(message = "Content field of comment cannot be blank")
	private String content;
}
