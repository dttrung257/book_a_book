package com.uet.book_a_book.models;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orderdetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orderdetail {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(nullable = false)
	private Long quantityOrdered;
	
	@Column(nullable = false)
	private Double priceEach;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
	private Book book;

	public Orderdetail(Long quantityOrdered, Double priceEach) {
		super();
		this.quantityOrdered = quantityOrdered;
		this.priceEach = priceEach;
	}
	
}
