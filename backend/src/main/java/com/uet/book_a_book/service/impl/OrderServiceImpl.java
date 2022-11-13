package com.uet.book_a_book.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.order.NewOrder;
import com.uet.book_a_book.dto.order.OrderAddedByAdmin;
import com.uet.book_a_book.dto.order.OrderDTO;
import com.uet.book_a_book.dto.order.OrderdetailDTO;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.entity.Order;
import com.uet.book_a_book.entity.Orderdetail;
import com.uet.book_a_book.entity.util.OrderStatus;
import com.uet.book_a_book.exception.book.NotFoundBookException;
import com.uet.book_a_book.exception.order.CannotCancelOrderException;
import com.uet.book_a_book.exception.order.CannotChangeOrderStatusException;
import com.uet.book_a_book.exception.order.NotFoundOrderException;
import com.uet.book_a_book.exception.order.NotFoundOrderStatusException;
import com.uet.book_a_book.repository.BookRepository;
import com.uet.book_a_book.repository.OrderRepository;
import com.uet.book_a_book.repository.OrderdetailRepository;
import com.uet.book_a_book.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderdetailRepository orderdetailRepository;
	@Autowired
	private BookRepository bookRepository;

	@Override
	public Order addOrder(NewOrder newOrder) {
		newOrder.getOrderdetails().stream().forEach(od -> {
			Book book = bookRepository.findById(od.getBookId()).orElse(null);
			if (book == null) {
				throw new NotFoundBookException("Not found book with id: " + od.getBookId());
			}
		});
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Order order = new Order();
		order.setAddress(newOrder.getAddress().trim());
		order.setOrderDate(new Date());
		order.setStatus(OrderStatus.STATUS_PENDING);
		order.setUser(user);
		orderRepository.save(order);
		newOrder.getOrderdetails().stream().forEach(od -> {
			Book book = bookRepository.findById(od.getBookId()).orElse(null);
			Orderdetail orderdetail = new Orderdetail();
			orderdetail.setQuantityOrdered(od.getQuantity());
			orderdetail.setPriceEach(book.getSellingPrice());
			orderdetail.setBook(book);
			orderdetail.setOrder(order);
			orderdetailRepository.save(orderdetail);
			book.setAvailableQuantity(book.getAvailableQuantity() - od.getQuantity());
			bookRepository.save(book);
		});

		return order;
	}

	@Override
	public Order addOrderFromAdmin(OrderAddedByAdmin newOrder) {
		newOrder.getOrderdetails().stream().forEach(od -> {
			Book book = bookRepository.findById(od.getBookId()).orElse(null);
			if (book == null) {
				throw new NotFoundBookException("Not found book with id: " + od.getBookId());
			}
		});
		Order order = new Order();
		order.setOrderDate(new Date());
		order.setStatus(OrderStatus.STATUS_SUCCESS);
		orderRepository.save(order);
		newOrder.getOrderdetails().stream().forEach(od -> {
			Book book = bookRepository.findById(od.getBookId()).orElse(null);
			Orderdetail orderdetail = new Orderdetail();
			orderdetail.setQuantityOrdered(od.getQuantity());
			orderdetail.setPriceEach(book.getSellingPrice());
			orderdetail.setBook(book);
			orderdetail.setOrder(order);
			orderdetailRepository.save(orderdetail);
			book.setAvailableQuantity(book.getAvailableQuantity() - od.getQuantity());
			book.setQuantityInStock(book.getQuantityInStock() - od.getQuantity());
			book.setQuantitySold(book.getQuantitySold() + od.getQuantity());
			bookRepository.save(book);
		});
		return order;
	}

	private OrderDTO orderToOrderDTO(Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setId(order.getId());
		orderDTO.setOrderDate(order.getOrderDate());
		orderDTO.setAddress(order.getAddress());
		orderDTO.setStatus(order.getStatus());
		AppUser user = orderRepository.findUserByOrderId(order.getId()).orElse(null);
		if (user == null) {
			orderDTO.setUser(null);
		} else {
			orderDTO.setUser(user.getEmail());
		}
		orderDTO.setQuantity(orderdetailRepository.countTotalQuantity(order.getId()));
		orderDTO.setTotal(orderdetailRepository.calculateTotalPrice(order.getId()));
		return orderDTO;
	}

	@Override
	public Page<OrderDTO> fetchUserOrder(Integer page, Integer size) {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.fetchUserOrders(user.getId());
		List<OrderDTO> orderDTOs = orders.stream().map(o -> orderToOrderDTO(o)).collect(Collectors.toList());
		return new PageImpl<>(orderDTOs, pageable, orderDTOs.size());
	}

	private OrderdetailDTO orderdetailToOrderdetailDTO(Orderdetail orderdetail) {
		OrderdetailDTO orderdetailDTO = new OrderdetailDTO();
		orderdetailDTO.setId(orderdetail.getId());
		orderdetailDTO.setPriceEach(orderdetail.getPriceEach());
		orderdetailDTO.setQuantityOrdered(orderdetail.getQuantityOrdered());
		Book book = orderdetailRepository.findBookByOrderdetailId(orderdetail.getId()).orElse(null);
		if (book == null) {
			orderdetailDTO.setImage(null);
			orderdetailDTO.setBookName(null);
		} else {
			orderdetailDTO.setImage(book.getImage());
			orderdetailDTO.setBookName(book.getName());
		}
		return orderdetailDTO;
	}

	@Override
	public Page<OrderdetailDTO> fetchOrderdetails(UUID orderId, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
		List<OrderdetailDTO> orderdetailDTOs = orderdetails.stream().map(od -> orderdetailToOrderdetailDTO(od))
				.collect(Collectors.toList());
		return new PageImpl<>(orderdetailDTOs, pageable, orderdetailDTOs.size());
	}

	@Override
	public void cancelOrder(UUID orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order == null) {
			throw new NotFoundOrderException("Not found order with id: " + orderId);
		}
		if (!order.getStatus().equals(OrderStatus.STATUS_PENDING)) {
			throw new CannotCancelOrderException("Orders can only be canceled in pending status");
		}
		List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
		orderdetails.forEach(od -> {
			Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
			if (book == null) {
				throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
			}
			book.setAvailableQuantity(book.getAvailableQuantity() + od.getQuantityOrdered());
			bookRepository.save(book);
			orderdetailRepository.delete(od);
		});
		orderRepository.delete(order);
	}

	@Override
	public Order updateStatus(UUID orderId, String status) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order == null) {
			throw new NotFoundOrderException("Not found order with id: " + orderId);
		}
		if (order.getStatus().equals(OrderStatus.STATUS_SUCCESS)) {
			throw new CannotChangeOrderStatusException("The status cannot be changed once the order id: "
					+ orderId.toString() + " has been successfully delivered");
		}
		// PENDING or SHIPPING to SUCCESS => quantity in stock decreases
		if ((order.getStatus().equals(OrderStatus.STATUS_PENDING)
				|| order.getStatus().equals(OrderStatus.STATUS_SHIPPING))
				&& status.equalsIgnoreCase(OrderStatus.STATUS_SUCCESS)) {

			order.setStatus(OrderStatus.STATUS_SUCCESS);
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				if (book == null) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
			});
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				book.setQuantityInStock(book.getQuantityInStock() - od.getQuantityOrdered());
				book.setQuantitySold(book.getQuantitySold() + od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		// CANCELED to SUCCESS => quantity in stock decreases, available quantity
		// decreases
		if (order.getStatus().equals(OrderStatus.STATUS_CANCELED)
				&& status.equalsIgnoreCase(OrderStatus.STATUS_SUCCESS)) {

			order.setStatus(OrderStatus.STATUS_SUCCESS);
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				if (book == null) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
			});
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				book.setQuantityInStock(book.getQuantityInStock() - od.getQuantityOrdered());
				book.setAvailableQuantity(book.getAvailableQuantity() - od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		// PENDING or SHIPPING to CANCELED => available quantity increases
		if ((order.getStatus().equals(OrderStatus.STATUS_PENDING)
				|| order.getStatus().equals(OrderStatus.STATUS_SHIPPING))
				&& status.equalsIgnoreCase(OrderStatus.STATUS_CANCELED)) {

			order.setStatus(OrderStatus.STATUS_CANCELED);
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				if (book == null) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
			});
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				book.setAvailableQuantity(book.getAvailableQuantity() + od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		// CANCELED to PENDING or SHIPPING => available quantity decreases
		if (order.getStatus().equals(OrderStatus.STATUS_CANCELED)
				&& (status.equalsIgnoreCase(OrderStatus.STATUS_PENDING)
						|| status.equalsIgnoreCase(OrderStatus.STATUS_SHIPPING))) {

			order.setStatus(status.toUpperCase());
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				if (book == null) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
			});
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				book.setAvailableQuantity(book.getAvailableQuantity() - od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		if ((order.getStatus().equals(OrderStatus.STATUS_PENDING) && status.equalsIgnoreCase(OrderStatus.STATUS_SHIPPING))
				|| (order.getStatus().equals(OrderStatus.STATUS_SHIPPING) && status.equalsIgnoreCase(OrderStatus.STATUS_PENDING))) {
			
			order.setStatus(status.toUpperCase());
		}
		if (!(status.equalsIgnoreCase(OrderStatus.STATUS_PENDING)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_SHIPPING)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_SUCCESS)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_CANCELED))) {
			throw new NotFoundOrderStatusException("Not found order status: " + status);
		}
		orderRepository.save(order);
		return order;
	}

	@Override
	public Page<OrderDTO> fetchAllOrders(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = orders.stream().map(o -> orderToOrderDTO(o)).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, orderDTOs.size());
	}
	
	@Override
	public Page<OrderDTO> fetchOrdersByUser(String email, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = orders.stream().map(o -> orderToOrderDTO(o))
				.filter(o -> (o.getUser().contains(email))).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, orderDTOs.size());
	}

	@Override
	public Page<OrderDTO> fetchOrdersByPrice(Double fromPrice, Double toPrice, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = orders.stream().map(o -> orderToOrderDTO(o))
				.filter(o -> (o.getTotal() >= fromPrice && o.getTotal() <= toPrice)).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, orderDTOs.size());
	}

	@Override
	public Page<OrderDTO> fetchOrdersByDate(Date orderDate, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = orders.stream().map(o -> orderToOrderDTO(o))
				.filter(o -> (DateUtils.isSameDay(orderDate, o.getOrderDate()))).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, orderDTOs.size());
	}

}
