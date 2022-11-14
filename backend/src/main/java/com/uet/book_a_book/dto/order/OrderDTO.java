package com.uet.book_a_book.dto.order;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	private String email;
	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", timezone = "GMT+7")
	private Date orderDate;
	private String address;
	private Long quantity;
	private Double total;
	private String status;
}
