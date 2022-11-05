package com.uet.book_a_book.dto.book;

import javax.validation.constraints.DecimalMin;
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
public class UpdateBook {
	@NotNull(message = "Id cannot be null")
	@Min(value = 1L, message = "Id is invalid")
	private Long id;
	
	@NotBlank(message = "Name cannot be blank")
	private String name;
	
	@NotBlank(message = "Category cannot be blank")
	private String category;
	
	@NotBlank(message = "Author cannot be blank")
	private String author;
	
	private String publisher;
	
	private String isbn;
	
	private Integer yearOfPublication;
	
	private Integer numberOfPages;
	
	@NotNull(message = "Buy price cannot be null")
	@DecimalMin(value = "0.1", message = "Buy price is invalid")
	private Double buyPrice;
	
	@NotNull(message = "Selling price cannot be null")
	@DecimalMin(value = "0.1", message = "Selling price is invalid")
	private Double sellingPrice;
	
	@NotNull(message = "Quantity cannot be null")
	@Min(value = 1L, message = "Quantity is invalid")
	private Long quantityInStock;
	
	private String desception;
	
	@NotBlank(message = "Image link cannot be blank")
	private String image;
	
	private Double width;
	
	private Double height;
}
