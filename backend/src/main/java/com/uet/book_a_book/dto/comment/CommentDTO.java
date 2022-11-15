package com.uet.book_a_book.dto.comment;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
	private UUID id;
	private UUID userId;
	private Long bookId;
	private String email;
	private String bookName;
	private Integer star;
	private String content;
}
