package com.uet.book_a_book.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uet.book_a_book.domain.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}