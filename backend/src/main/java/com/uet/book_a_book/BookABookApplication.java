package com.uet.book_a_book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.uet.book_a_book")
public class BookABookApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookABookApplication.class, args);
	}
}
