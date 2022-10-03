package com.uet.book_a_book.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String author;
	
	@Column(name = "buyPrice", nullable = false)
	private double buyPrice;
	
	@Column(name = "sellingPrice", nullable = false)
	private double sellingPrice;
	
	private String description;
	
	@Column(name = "quantityInStock", nullable = false)
	private Long quantityInStock;
	
	@Column(name = "quantitySold", nullable = false)
	private Long quantitySold;
	
	private double rating;
	private Long numberOfReviews;
	
	@OneToMany
	@JoinColumn(name = "book_id")
	private List<Comment> comments  = new ArrayList<>();
}
