package com.uet.book_a_book.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.order.NewOrder;
import com.uet.book_a_book.dto.order.OrderAddedByAdmin;
import com.uet.book_a_book.dto.order.UpdateOrderStatus;
import com.uet.book_a_book.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping("/order/add_order")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public ResponseEntity<Object> addOrder(@Valid @RequestBody NewOrder newOrder) {
		return ResponseEntity.ok(orderService.addOrder(newOrder));
	}

	@GetMapping("/order/fetch_user_order")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public ResponseEntity<Object> fetchUserOrder(
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page field must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size field must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(orderService.fetchUserOrder(Integer.parseInt(page), Integer.parseInt(size)));
	}

	@GetMapping("/order/fetch_orderdetails")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public ResponseEntity<Object> fetchOrderdetail(
			@RequestParam(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "Order id field must in UUID format") String id,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page field must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size field must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(
				orderService.fetchOrderdetails(UUID.fromString(id), Integer.parseInt(page), Integer.parseInt(size)));
	}

	@DeleteMapping("/order/cancel_order")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public ResponseEntity<Object> cancelOrder(
			@RequestParam(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Order id field must in UUID format") 
			String id) {
		orderService.cancelOrder(UUID.fromString(id));
		return ResponseEntity.ok("Cancel order successfully");
	}

	@PutMapping("/manage_order/update_status")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> updateStatus(@Valid @RequestBody UpdateOrderStatus updateOrderStatus) {
		return ResponseEntity.ok(orderService.updateStatus(UUID.fromString(updateOrderStatus.getOrderId()),
				updateOrderStatus.getStatus()));
	}

	@PostMapping("/manage_order/add_order")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> createOrderFromAdmin(@Valid @RequestBody OrderAddedByAdmin newOrder) {
		return ResponseEntity.ok(orderService.addOrderFromAdmin(newOrder));
	}

	@GetMapping("/manage_order/fetch_all_orders")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> fetchAllOrders(
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page field must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size field must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(orderService.fetchAllOrders(Integer.parseInt(page), Integer.parseInt(size)));
	}
	
	@GetMapping("/manage_order/fetch_orders_by_email")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> fetchOrdersByUser(
			@RequestParam("email") @Email(message = "Email is not valid") String email,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page field must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size field must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(orderService.fetchOrdersByUser(email, Integer.parseInt(page), Integer.parseInt(size)));
	}

	@GetMapping("/manage_order/fetch_orders_by_price")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> fetchOrdersByPrice(
			@RequestParam(name = "from", required = false, defaultValue = "0") 
			@DecimalMin(value = "0.0", message = "Lower price field must be in double format greater than or equal to 0") String fromPrice,
			@RequestParam(name = "to", required = false, defaultValue = "100000000") 
			@DecimalMin(value = "0.1", message = "Higher price field must be in double format greater than or equal to 0.1") String toPrice,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page field must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size field must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(orderService.fetchOrdersByPrice(Double.parseDouble(fromPrice),
				Double.parseDouble(toPrice), Integer.parseInt(page), Integer.parseInt(size)));
	}
	
	@GetMapping("/manage_order/fetch_orders_by_date")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Object> fetchOrdersByDate(
			@RequestParam("date") 
			@DateTimeFormat(pattern="dd-MM-yyyy") Date orderDate,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page field must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size field must be in integer format greater than or equal to 1") String size) throws NumberFormatException, ParseException {
		return ResponseEntity.ok(orderService.fetchOrdersByDate((orderDate), Integer.parseInt(page), Integer.parseInt(size)));
	}
}
