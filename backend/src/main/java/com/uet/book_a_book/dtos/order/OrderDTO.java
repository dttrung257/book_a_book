package com.uet.book_a_book.dtos.order;

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
public class OrderDTO {
	private UUID id;
	private UUID userId;
	private String fullName;
	private String email;
	@JsonFormat(pattern = Const.DEFAULT_DATETIME_FORMAT, timezone = Const.DEFAULT_TIMEZONE)
	private Date orderDate;
	private String address;
	private String phoneNumber;
	private Long quantity;
	private Double total;
	private String status;
}
