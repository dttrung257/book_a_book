package com.uet.book_a_book.controller;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.order.AdmOrder;
import com.uet.book_a_book.dto.order.NewOrder;
import com.uet.book_a_book.dto.order.OrderDTO;
import com.uet.book_a_book.dto.order.OrderdetailDTO;
import com.uet.book_a_book.dto.order.UpdateOrderStatus;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.constant.Const;
import com.uet.book_a_book.service.OrderService;
import com.uet.book_a_book.validator.IdConstraint;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Validated
@Slf4j
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/orders")
	public ResponseEntity<OrderDTO> addOrder(@Valid @RequestBody NewOrder newOrder) {
		return ResponseEntity.ok(orderService.addOrder(newOrder));
	}

	@GetMapping("/orders")
	public ResponseEntity<Page<OrderDTO>> getUserOrders(
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getUserOrders(page, size));
	}

	@GetMapping("/orders/{id}/orderdetails")
	public ResponseEntity<Page<OrderdetailDTO>> getOrderdetails(
			@PathVariable(name = "id", required = true) @IdConstraint String id,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(
				orderService.getOrderdetails(UUID.fromString(id), page, size));
	}

	@DeleteMapping("/orders/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable(name = "id", required = true) @IdConstraint String id) {
		orderService.cancelOrder(UUID.fromString(id));
		log.info("User id: {} canceled order id: {}.", 
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), id);
		return ResponseEntity.ok("Cancel order successfully");
	}
	
	@PostMapping("/manage/orders")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<OrderDTO> addOrderByAdmin(@Valid @RequestBody AdmOrder admOrder) {
		return ResponseEntity.ok(orderService.addOrderByAdmin(admOrder));
	}

	@PutMapping("/manage/orders/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<OrderDTO> updateStatus(
			@PathVariable(name = "id", required = true) @IdConstraint String id,
			@Valid @RequestBody UpdateOrderStatus updateOrderStatus) {
		return ResponseEntity.ok(orderService.updateStatus(UUID.fromString(id), updateOrderStatus.getStatus().trim()));
	}
	
	@GetMapping("/manage/orders/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<OrderDTO> getOrderById(
			@PathVariable(name = "id", required = true) @IdConstraint String id) {
		return ResponseEntity.ok(orderService.getOrderById(UUID.fromString(id)));
	}
	
	@GetMapping("/manage/orders")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Page<OrderDTO>> getOrdersByFilter(
			@RequestParam(name = "user_id", required = false, defaultValue = "") String userId,
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "status", required = false, defaultValue = "") String status,
			@RequestParam(name = "from", required = false, defaultValue = Const.DEFAULT_MIN_PRICE) @DecimalMin(value = "0.0") Double fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = Const.DEFAULT_MAX_PRICE) @DecimalMin(value = "0.1") Double toPrice,
			@RequestParam(name = "date") @DateTimeFormat(pattern="dd-MM-yyyy") Optional<Date> orderDate,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getOrders(userId.trim(), name.trim(), status.trim(), fromPrice, toPrice, orderDate.orElse(null), page, size));
	}
	
	@DeleteMapping("manage/orders/{id}")
	public ResponseEntity<String> deleteOrder(
			@PathVariable(name = "id", required = true) @IdConstraint String id) {
		orderService.deleteOrder(UUID.fromString(id));
		log.info("Admin id: {} deleted order id: {}.", 
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), id);
		return ResponseEntity.ok("Delete order id: " + id + " successfully");
	}
	
}
