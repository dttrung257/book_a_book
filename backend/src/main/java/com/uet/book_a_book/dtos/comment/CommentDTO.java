package com.uet.book_a_book.dtos.comment;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uet.book_a_book.models.constant.Const;

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
	private String fullName;
	private String avatar;
	private String bookName;
	private Integer star;
	private String content;
	@JsonFormat(pattern = Const.COMMENT_DATETIME_FORMAT, timezone = Const.DEFAULT_TIMEZONE)
	private Date createdAt;
	@JsonFormat(pattern = Const.COMMENT_DATETIME_FORMAT, timezone = Const.DEFAULT_TIMEZONE)
	private Date updatedAt;
}
