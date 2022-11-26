package com.uet.book_a_book.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.uet.book_a_book.dto.order.UpdateOrderStatus;
import com.uet.book_a_book.service.OrderService;
import com.uet.book_a_book.validator.IdConstraint;

@RestController
@RequestMapping("/api")
@Validated
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/orders")
	public ResponseEntity<Object> addOrder(@Valid @RequestBody NewOrder newOrder) {
		return ResponseEntity.ok(orderService.addOrder(newOrder));
	}

	@GetMapping("/orders")
	public ResponseEntity<Object> getUserOrders(
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getUserOrders(page, size));
	}

	@GetMapping("/orders/{id}/orderdetails")
	public ResponseEntity<Object> getOrderdetails(
			@PathVariable(name = "id", required = true) @IdConstraint String id,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(
				orderService.getOrderdetails(UUID.fromString(id), page, size));
	}

	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Object> cancelOrder(@PathVariable(name = "id", required = true) @IdConstraint String id) {
		orderService.cancelOrder(UUID.fromString(id));
		return ResponseEntity.ok("Cancel order successfully");
	}

	@PutMapping("/manage/orders/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> updateStatus(
			@PathVariable(name = "id", required = true) @IdConstraint String id,
			@Valid @RequestBody UpdateOrderStatus updateOrderStatus) {
		return ResponseEntity.ok(orderService.updateStatus(UUID.fromString(id), updateOrderStatus.getStatus()));
	}

	@PostMapping("/manage/orders")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> addOrderByAdmin(@Valid @RequestBody AdmOrder admOrder) {
		return ResponseEntity.ok(orderService.addOrderByAdmin(admOrder));
	}

	@GetMapping("/manage/orders")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getAllOrders(
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getAllOrders(page, size));
	}
	
	@GetMapping("/manage/orders/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getOrderById(
			@PathVariable(name = "id", required = true) @IdConstraint String id) {
		return ResponseEntity.ok(orderService.getOrderById(UUID.fromString(id)));
	}
	
	@GetMapping("/manage/orders/user")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getOrdersByUserId(
			@RequestParam(name = "user_id", required = true) @IdConstraint String userId,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getOrdersByUserId(UUID.fromString(userId), page, size));
	}
	
	@GetMapping("/manage/orders/email")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getOrdersByEmail(
			@RequestParam("email") @NotBlank(message = "email field is mandatory") String email,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getOrdersByEmail(email, page, size));
	}

	@GetMapping("/manage/orders/price")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getOrdersByPrice(
			@RequestParam(name = "from", required = false, defaultValue = "0") @DecimalMin(value = "0.0") Double fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = "100000000000") @DecimalMin(value = "0.1") Double toPrice,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getOrdersByPrice(fromPrice, toPrice, page, size));
	}
	
	@GetMapping("/manage/orders/date")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getOrdersByDate(
			@RequestParam("date") @DateTimeFormat(pattern="dd-MM-yyyy") Date orderDate,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) 
					throws NumberFormatException, ParseException {
		return ResponseEntity.ok(orderService.getOrdersByDate(orderDate, page, size));
	}
	
	@GetMapping("/manage/orders/filter")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getOrdersByFilter(
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "from", required = false, defaultValue = "0") @DecimalMin(value = "0.0") Double fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = "100000000000") @DecimalMin(value = "0.1") Double toPrice,
			@RequestParam(name = "date") @DateTimeFormat(pattern="dd-MM-yyyy") Optional<Date> orderDate,
			@RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") @Min(value = 1) Integer size) 
					throws NumberFormatException, ParseException {
		return ResponseEntity.ok(orderService.getOrdersByFilter(name, fromPrice, toPrice, orderDate.orElse(null), page, size));
	}
	
	@DeleteMapping("manage/orders/{id}")
	public ResponseEntity<Object> deleteOrder(
			@PathVariable(name = "id", required = true) @IdConstraint String id) {
		orderService.deleteOrder(UUID.fromString(id));
		return ResponseEntity.ok("Delete order id: " + id + " successfully");
	}
	
}
