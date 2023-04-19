package com.uet.book_a_book.dtos.order;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderdetailDTO {
	private UUID id;
	private String image;
	private String bookName;
	private Long quantityOrdered;
	private Double priceEach;
}
