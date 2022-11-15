package com.uet.book_a_book.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/orders")
	public ResponseEntity<Object> addOrder(@Valid @RequestBody NewOrder newOrder) {
		return ResponseEntity.ok(orderService.addOrder(newOrder));
	}

	@GetMapping("/orders")
	public ResponseEntity<Object> getUserOrders(
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getUserOrders(page, size));
	}

	@GetMapping("/orders/{id}/orderdetails")
	public ResponseEntity<Object> getOrderdetails(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") String id,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) {
		return ResponseEntity.ok(
				orderService.getOrderdetails(UUID.fromString(id), page, size));
	}

	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Object> cancelOrder(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") 
			String id) {
		orderService.cancelOrder(UUID.fromString(id));
		return ResponseEntity.ok("Cancel order successfully");
	}

	@PutMapping("/manage/orders/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> updateStatus(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") String id,
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
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getAllOrders(page, size));
	}
	
	@GetMapping("/manage/orders/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getOrderById(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") String id) {
		return ResponseEntity.ok(orderService.getOrderById(UUID.fromString(id)));
	}
	
	@GetMapping("/manage/orders/email")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getOrdersByEmail(
			@RequestParam("email") 
			@NotBlank(message = "email field is mandatory") String email,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getOrdersByEmail(email, page, size));
	}

	@GetMapping("/manage/orders/price")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> getOrdersByPrice(
			@RequestParam(name = "from", required = false, defaultValue = "0") 
			@DecimalMin(value = "0.0") Double fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = "100000000") 
			@DecimalMin(value = "0.1") Double toPrice,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) {
		return ResponseEntity.ok(orderService.getOrdersByPrice(fromPrice, toPrice, page, size));
	}
	
	@GetMapping("/manage/orders/date")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> fetchOrdersByDate(
			@RequestParam("date") 
			@DateTimeFormat(pattern="dd-MM-yyyy") Date orderDate,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) throws NumberFormatException, ParseException {
		return ResponseEntity.ok(orderService.getOrdersByDate((orderDate), page, size));
	}
}
