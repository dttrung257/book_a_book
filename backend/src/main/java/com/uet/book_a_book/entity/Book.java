package com.uet.book_a_book.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Lob
	@Column(nullable = false)
	private String image;
	
	@Column(nullable = false)
	private String category;
	
	@Column(nullable = false)
	private String author;
	
	@Column(nullable = true)
	private Double width;
	
	@Column(nullable = true)
	private Double height;
	
	@Column(nullable = true)
	private String isbn;

	@Column(nullable = true)
	private String publisher;
	
	@Column(nullable = true)
	private Integer numberOfPages;
	
	@Column(nullable = true)
	private Integer yearOfPublication;
	
	@Column(nullable = false)
	private Double buyPrice;
	
	@Column(nullable = false)
	private Double sellingPrice;
	
	@Column(nullable = true)
	private String description;
	
	@Column(nullable = false)
	private Long quantityInStock;
	
	@Column(nullable = false)
	private Long availableQuantity;
	
	@Column(nullable = false)
	private Long quantitySold;
	
	@Column(nullable = false)
	private boolean stopSelling = false;
	
	@Column(nullable = true)
	private Double rating;
	
	@JsonIgnore
	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> comments;
	
	@JsonIgnore
	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Orderdetail> orderdetails;

	public Book(String name, String image, String category, String author, Double width, Double height, String isbn,
			String publisher, Integer numberOfPages, Integer yearOfPublication, double buyPrice, double sellingPrice,
			String description, Long quantityInStock, Long availableQuantity, Long quantitySold, boolean stopSelling,
			Double rating, List<Comment> comments, List<Orderdetail> orderdetails) {
		super();
		this.name = name;
		this.image = image;
		this.category = category;
		this.author = author;
		this.width = width;
		this.height = height;
		this.isbn = isbn;
		this.publisher = publisher;
		this.numberOfPages = numberOfPages;
		this.yearOfPublication = yearOfPublication;
		this.buyPrice = buyPrice;
		this.sellingPrice = sellingPrice;
		this.description = description;
		this.quantityInStock = quantityInStock;
		this.availableQuantity = availableQuantity;
		this.quantitySold = quantitySold;
		this.stopSelling = stopSelling;
		this.rating = rating;
		this.comments = comments;
		this.orderdetails = orderdetails;
	}
	
	
}
