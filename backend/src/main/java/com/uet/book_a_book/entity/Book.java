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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Lob
	private String image;
	
	@Column(nullable = false)
	private String category;
	
	@Column(nullable = false)
	private String author;
	
	@Column(nullable = true)
	private double width;
	
	@Column(nullable = true)
	private double height;
	
	private String isbn;
	private String publisher;
	
	@Column(nullable = true)
	private int numberOfPages;
	
	@Column(nullable = true)
	private int yearOfPublication;
	
	@Column(nullable = false)
	private double buyPrice;
	
	@Column(nullable = false)
	private double sellingPrice;
	
	private String description;
	
	@Column(nullable = false)
	private Long quantityInStock;
	
	@Column(nullable = false)
	private Long quantitySold;
	
	@Column(nullable = false)
	private boolean stopSelling = false;
	
	private double rating;
	
	@JsonIgnore
	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> comments;
	
	@JsonIgnore
	@OneToOne(mappedBy = "book")
	private Orderdetail orderdetail;
}
