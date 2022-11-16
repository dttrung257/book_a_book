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

import com.uet.book_a_book.dto.order.AdmOrder;
import com.uet.book_a_book.dto.order.NewOrder;
import com.uet.book_a_book.dto.order.OrderDTO;
import com.uet.book_a_book.dto.order.OrderdetailDTO;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Book;
import com.uet.book_a_book.entity.Order;
import com.uet.book_a_book.entity.Orderdetail;
import com.uet.book_a_book.entity.constant.OrderStatus;
import com.uet.book_a_book.exception.book.NotEnoughQuantityException;
import com.uet.book_a_book.exception.book.NotFoundBookException;
import com.uet.book_a_book.exception.order.CannotCancelOrderException;
import com.uet.book_a_book.exception.order.CannotChangeOrderStatusException;
import com.uet.book_a_book.exception.order.NotFoundOrderException;
import com.uet.book_a_book.exception.order.NotFoundOrderStatusException;
import com.uet.book_a_book.mapper.OrderMapper;
import com.uet.book_a_book.mapper.OrderdetailMapper;
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
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderdetailMapper orderdetailMapper;

	@Override
	public Order addOrder(NewOrder newOrder) {
		newOrder.getOrderdetails().stream().forEach(od -> {
			Book book = bookRepository.findById(od.getBookId()).orElse(null);
			if (book == null) {
				throw new NotFoundBookException("Not found book id: " + od.getBookId());
			}
			if (book.getAvailableQuantity() < od.getQuantity()) {
				throw new NotEnoughQuantityException("Not enough quantity book: " + book.getName() + " to order");
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
	public Order addOrderByAdmin(AdmOrder newOrder) {
		newOrder.getOrderdetails().stream().forEach(od -> {
			Book book = bookRepository.findById(od.getBookId()).orElse(null);
			if (book == null) {
				throw new NotFoundBookException("Not found book id: " + od.getBookId());
			}
			if (book.getAvailableQuantity() < od.getQuantity()) {
				throw new NotEnoughQuantityException("Not enough quantity book: " + book.getName() + " to order");
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

	@Override
	public Page<OrderDTO> getUserOrders(Integer page, Integer size) {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findOrdersByUserId(user.getId());
		List<OrderDTO> orderDTOs = orders.stream().
									map(o -> orderMapper.mapToOrderDTO(o)).collect(Collectors.toList());
		return new PageImpl<>(orderDTOs, pageable, orderDTOs.size());
	}

	@Override
	public Page<OrderdetailDTO> getOrderdetails(UUID orderId, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
		List<OrderdetailDTO> orderdetailDTOs = orderdetails.stream().map(od -> orderdetailMapper.mapToOrderdetailDTO(od))
				.collect(Collectors.toList());
		return new PageImpl<>(orderdetailDTOs, pageable, orderdetailDTOs.size());
	}

	@Override
	public void cancelOrder(UUID orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order == null) {
			throw new NotFoundOrderException("Not found order id: " + orderId);
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
			throw new NotFoundOrderException("Not found order id: " + orderId);
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
	public Page<OrderDTO> getOrdersByUserId(UUID userId, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findOrdersByUserId(userId);
		List<OrderDTO> orderDTOs = orders.stream().
									map(o -> orderMapper.mapToOrderDTO(o)).collect(Collectors.toList());
		return new PageImpl<>(orderDTOs, pageable, orderDTOs.size());
	}

	@Override
	public Page<OrderDTO> getAllOrders(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = orders.stream().map(o -> orderMapper.mapToOrderDTO(o)).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, orderDTOs.size());
	}
	
	@Override
	public Page<OrderDTO> getOrdersByEmail(String email, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = orders.stream().map(o -> orderMapper.mapToOrderDTO(o))
				.filter(o -> (o.getEmail().contains(email))).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, orderDTOs.size());
	}

	@Override
	public Page<OrderDTO> getOrdersByPrice(Double fromPrice, Double toPrice, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = orders.stream().map(o -> orderMapper.mapToOrderDTO(o))
				.filter(o -> (o.getTotal() >= fromPrice && o.getTotal() <= toPrice)).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, orderDTOs.size());
	}

	@Override
	public Page<OrderDTO> getOrdersByDate(Date orderDate, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = orders.stream().map(o -> orderMapper.mapToOrderDTO(o))
				.filter(o -> (DateUtils.isSameDay(orderDate, o.getOrderDate()))).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, orderDTOs.size());
	}

	@Override
	public OrderDTO getOrderById(UUID id) {
		Order order = orderRepository.findById(id).orElse(null);
		if (order == null) {
			throw new NotFoundOrderException("Not found order id: " + id);
		}
		return orderMapper.mapToOrderDTO(order);
	}

	

}
