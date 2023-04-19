package com.uet.book_a_book.dtos.book;

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
	@NotBlank(message = "name field is mandatory")
	private String name;
	
	@NotBlank(message = "category field is mandatory")
	private String category;
	
	@NotBlank(message = "author field is mandatory")
	private String author;
	
	private String publisher;
	
	private String isbn;
	
	private Integer yearOfPublication;
	
	private Integer numberOfPages;
	
	@NotNull(message = "buyPrice field is mandatory")
	@DecimalMin(value = "0.1")
	private Double buyPrice;
	
	@NotNull(message = "sellingPrice field is mandatory")
	@DecimalMin(value = "0.1")
	private Double sellingPrice;
	
	@NotNull(message = "quantityInStock field is mandatory")
	@Min(value = 1L, message = "quantity field must be greater than or equal to 1")
	private Long quantityInStock;
	
	private String description;
	
	@NotBlank(message = "image field is mandatory")
	private String image;
	
	private Double width;
	
	private Double height;
}
