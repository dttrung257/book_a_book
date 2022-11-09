package com.uet.book_a_book.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "uuid-char")
	private UUID id;
	
	@Column(nullable = false)
	private String content;
	
	@Column(nullable = false)
	@Temporal(value = TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", timezone = "GMT+7")
	private Date createdAt;
	
	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", timezone = "GMT+7")
	private Date updatedAt;
	
	@Column(nullable = false)
	private Integer star;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", referencedColumnName = "id")
	private Book book;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private AppUser user;
}
