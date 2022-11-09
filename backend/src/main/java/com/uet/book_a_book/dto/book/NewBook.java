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
public class NewBook {
	@NotBlank(message = "Name field cannot be blank")
	private String name;
	
	@NotBlank(message = "Category field cannot be blank")
	private String category;
	
	@NotBlank(message = "Author field cannot be blank")
	private String author;
	
	private String publisher;
	
	private String isbn;
	
	private Integer yearOfPublication;
	
	private Integer numberOfPages;
	
	@NotNull(message = "Buy price field cannot be null")
	@DecimalMin(value = "0.1", message = "Buy price field is invalid")
	private Double buyPrice;
	
	@NotNull(message = "Selling price field cannot be null")
	@DecimalMin(value = "0.1", message = "Selling price field is invalid")
	private Double sellingPrice;
	
	@NotNull(message = "Quantity field cannot be null")
	@Min(value = 1L, message = "Quantity field is invalid")
	private Long quantityInStock;
	
	private String description;
	
	@NotBlank(message = "Image link field cannot be blank")
	private String image;
	
	private Double width;
	
	private Double height;
}
