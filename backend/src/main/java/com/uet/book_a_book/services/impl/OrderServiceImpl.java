package com.uet.book_a_book.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dtos.order.AdmOrder;
import com.uet.book_a_book.dtos.order.NewOrder;
import com.uet.book_a_book.dtos.order.OrderDTO;
import com.uet.book_a_book.dtos.order.OrderdetailDTO;
import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.models.Book;
import com.uet.book_a_book.models.Order;
import com.uet.book_a_book.models.Orderdetail;
import com.uet.book_a_book.models.constant.Const;
import com.uet.book_a_book.models.constant.OrderStatus;
import com.uet.book_a_book.exception.book.NotEnoughQuantityException;
import com.uet.book_a_book.exception.book.NotFoundBookException;
import com.uet.book_a_book.exception.order.CannotCancelOrderException;
import com.uet.book_a_book.exception.order.CannotDeleteShippingOrderException;
import com.uet.book_a_book.exception.order.CannotDeleteSuccessOrderException;
import com.uet.book_a_book.exception.order.NotFoundOrderException;
import com.uet.book_a_book.exception.order.NotFoundOrderStatusException;
import com.uet.book_a_book.mapper.OrderMapper;
import com.uet.book_a_book.mapper.OrderdetailMapper;
import com.uet.book_a_book.repositories.BookRepository;
import com.uet.book_a_book.repositories.OrderRepository;
import com.uet.book_a_book.repositories.OrderdetailRepository;
import com.uet.book_a_book.services.OrderService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final OrderdetailRepository orderdetailRepository;
	private final BookRepository bookRepository;
	private final OrderMapper orderMapper;
	private final OrderdetailMapper orderdetailMapper;

	/** Get all orders of a user. **/
	@Override
	public Page<OrderDTO> getUserOrders(Integer page, Integer size) {
		final AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		final Pageable pageable = PageRequest.of(page, size);
		final List<Order> orders = orderRepository.findOrdersByUserId(user.getId());
		final List<OrderDTO> orderDTOs = orders.stream()
				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.map(o -> orderMapper.mapToOrderDTO(o))
				.collect(Collectors.toList());
		final int start = (int) pageable.getOffset();
		final int end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(orderDTOs, pageable, orderDTOs.size());
	}

	/** Get all order details of a order. **/
	@Override
	public Page<OrderdetailDTO> getOrderdetails(UUID orderId, Integer page, Integer size) {
		final Pageable pageable = PageRequest.of(page, size);
		final List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
		final List<OrderdetailDTO> orderdetailDTOs = orderdetails.stream()
				.map(od -> orderdetailMapper.mapToOrderdetailDTO(od)).collect(Collectors.toList());
		return new PageImpl<>(orderdetailDTOs, pageable, orderdetailDTOs.size());
	}

	/** Get orders by multiple filters. **/
	@Override
	public Page<OrderDTO> getOrders(String userId, String name, String status, Double fromPrice, Double toPrice,
									Date orderDate, Integer page, Integer size) {
		final Pageable pageable = PageRequest.of(page, size);
		final List<Order> orders = orderRepository.findAll();
		List<OrderDTO> orderDTOs = orders.stream()
				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.map(o -> orderMapper.mapToOrderDTO(o)).collect(Collectors.toList());
		if (userId.matches(Const.UUID_REGEX)) {
			orderDTOs = orderDTOs.stream()
					.filter(oDTO -> (oDTO.getUserId() != null && oDTO.getUserId().equals(UUID.fromString(userId))))
					.collect(Collectors.toList());
		}
		if (!name.equals("") && name != null) {
			orderDTOs = orderDTOs.stream()
					.filter(oDTO -> ((oDTO.getFullName() != null
							&& oDTO.getFullName().toLowerCase().contains(name.toLowerCase()))
							|| (oDTO.getEmail() != null && oDTO.getEmail().toLowerCase().contains(name.toLowerCase()))))
					.collect(Collectors.toList());
		}
		if (status.equalsIgnoreCase(OrderStatus.STATUS_PENDING) || status.equalsIgnoreCase(OrderStatus.STATUS_SHIPPING)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_SUCCESS)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_CANCELED)) {
			orderDTOs = orderDTOs.stream().filter(oDTO -> (oDTO.getStatus()).equalsIgnoreCase(status))
					.collect(Collectors.toList());
		}
		if (orderDate != null) {
			orderDTOs = orderDTOs.stream().filter(oDTO -> (DateUtils.isSameDay(oDTO.getOrderDate(), orderDate)))
					.collect(Collectors.toList());
		}
		orderDTOs = orderDTOs.stream().filter(oDTO -> (oDTO.getTotal() >= fromPrice && oDTO.getTotal() <= toPrice))
				.collect(Collectors.toList());
		final int start = (int) pageable.getOffset();
		final int end = Math.min((start + pageable.getPageSize()), orderDTOs.size());
		if (start <= orderDTOs.size()) {
			return new PageImpl<>(orderDTOs.subList(start, end), pageable, orderDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, orderDTOs.size());
	}

	/** Get order by id. **/
	@Override
	public OrderDTO getOrderById(UUID id) {
		final Optional<Order> order = orderRepository.findById(id);
		if (order.isEmpty()) {
			throw new NotFoundOrderException("Not found order id: " + id);
		}
		return orderMapper.mapToOrderDTO(order.get());
	}

	/** User orders online. **/
	@Override
	public OrderDTO addOrder(NewOrder newOrder) {
		newOrder.getOrderdetails().stream().forEach(od -> {
			Book book = bookRepository.findById(od.getBookId()).orElse(null);
			if (book == null) {
				throw new NotFoundBookException("Not found book id: " + od.getBookId());
			}
			if (book.getAvailableQuantity() < od.getQuantity()) {
				throw new NotEnoughQuantityException("Not enough quantity book: " + book.getName() + " to order");
			}
		});
		final AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Order order = new Order();
		order.setAddress(newOrder.getAddress().trim());
		order.setPhoneNumber(newOrder.getPhoneNumber());
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
		log.info("User id: {} added new order id: {}.", user.getId(), order.getId());
		return orderMapper.mapToOrderDTO(order);
	}

	/** Users buy books at the store **/
	@Transactional
	@Override
	public OrderDTO addOrderByAdmin(AdmOrder newOrder) {
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
		newOrder.getOrderdetails().forEach(od -> {
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
		log.info("Admin id: {} added new order id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), order.getId());
		return orderMapper.mapToOrderDTO(order);
	}

	/** User cancels order. **/
	@Transactional
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
			Optional<Book> book = orderdetailRepository.findBookByOrderdetailId(od.getId());
			if (book.isEmpty()) {
				throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
			}
		});
		orderdetails.forEach(od -> {
			Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
			book.setAvailableQuantity(book.getAvailableQuantity() + od.getQuantityOrdered());
			bookRepository.save(book);
		});
		order.setStatus(OrderStatus.STATUS_CANCELED);
		orderRepository.save(order);
	}

	/** Change order status. **/
	@Transactional
	@Override
	public OrderDTO updateStatus(UUID orderId, String status) {
		Order order = orderRepository.findById(orderId).orElse(null);
		if (order == null) {
			throw new NotFoundOrderException("Not found order id: " + orderId);
		}
		status = status.trim();
		if (order.getStatus().equalsIgnoreCase(status)) {
			return orderMapper.mapToOrderDTO(order);
		}
		if (!(status.equalsIgnoreCase(OrderStatus.STATUS_PENDING)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_SHIPPING)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_SUCCESS)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_CANCELED))) {
			throw new NotFoundOrderStatusException("Not found order status: " + status);
		}
		String beforeStatus = order.getStatus();
		// PENDING or SHIPPING to SUCCESS => quantity in stock decreased.
		if ((order.getStatus().equals(OrderStatus.STATUS_PENDING)
				|| order.getStatus().equals(OrderStatus.STATUS_SHIPPING))
				&& status.equalsIgnoreCase(OrderStatus.STATUS_SUCCESS)) {

			order.setStatus(OrderStatus.STATUS_SUCCESS);
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Optional<Book> book = orderdetailRepository.findBookByOrderdetailId(od.getId());
				if (book.isEmpty()) {
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
		// CANCELED to SUCCESS => quantity in stock decreases, available quantity decreases.
		if (order.getStatus().equals(OrderStatus.STATUS_CANCELED)
				&& status.equalsIgnoreCase(OrderStatus.STATUS_SUCCESS)) {

			order.setStatus(OrderStatus.STATUS_SUCCESS);
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Optional<Book> book = orderdetailRepository.findBookByOrderdetailId(od.getId());
				if (book.isEmpty()) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
			});
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				book.setQuantityInStock(book.getQuantityInStock() - od.getQuantityOrdered());
				book.setAvailableQuantity(book.getAvailableQuantity() - od.getQuantityOrdered());
				book.setQuantitySold(book.getQuantitySold() + od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		// PENDING or SHIPPING to CANCELED => available quantity increased.
		if ((order.getStatus().equals(OrderStatus.STATUS_PENDING)
				|| order.getStatus().equals(OrderStatus.STATUS_SHIPPING))
				&& status.equalsIgnoreCase(OrderStatus.STATUS_CANCELED)) {

			order.setStatus(OrderStatus.STATUS_CANCELED);
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Optional<Book> book = orderdetailRepository.findBookByOrderdetailId(od.getId());
				if (book.isEmpty()) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
			});
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				book.setAvailableQuantity(book.getAvailableQuantity() + od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		// CANCELED to PENDING or SHIPPING => available quantity decreased.
		if (order.getStatus().equals(OrderStatus.STATUS_CANCELED)
				&& (status.equalsIgnoreCase(OrderStatus.STATUS_PENDING)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_SHIPPING))) {

			order.setStatus(status.toUpperCase());
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Optional<Book> book = orderdetailRepository.findBookByOrderdetailId(od.getId());
				if (book.isEmpty()) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
			});
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				book.setAvailableQuantity(book.getAvailableQuantity() - od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		// SUCCESS TO PENDING, SHIPPING => Quantity in stock increased, QuantitySold
		// decreased.
		if (order.getStatus().equals(OrderStatus.STATUS_SUCCESS) && (status.equalsIgnoreCase(OrderStatus.STATUS_PENDING)
				|| status.equalsIgnoreCase(OrderStatus.STATUS_SHIPPING))) {
			order.setStatus(status.toUpperCase());
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Optional<Book> book = orderdetailRepository.findBookByOrderdetailId(od.getId());
				if (book.isEmpty()) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
			});
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				book.setQuantityInStock(book.getQuantityInStock() + od.getQuantityOrdered());
				book.setQuantitySold(book.getQuantitySold() - od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		// SUCCESS TO CANCELED => Quantity in stock increased, Available quantity
		// increased, Quantity sold decreased.
		if (order.getStatus().equals(OrderStatus.STATUS_SUCCESS)
				&& (status.equalsIgnoreCase(OrderStatus.STATUS_CANCELED))) {
			order.setStatus(status.toUpperCase());
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(orderId);
			orderdetails.forEach(od -> {
				Optional<Book> book = orderdetailRepository.findBookByOrderdetailId(od.getId());
				if (book.isEmpty()) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
			});
			orderdetails.forEach(od -> {
				Book book = orderdetailRepository.findBookByOrderdetailId(od.getId()).orElse(null);
				book.setQuantityInStock(book.getQuantityInStock() + od.getQuantityOrdered());
				book.setAvailableQuantity(book.getAvailableQuantity() + od.getQuantityOrdered());
				book.setQuantitySold(book.getQuantitySold() - od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		// SHIPPING TO PENDING Or PENDING TO SHIPPING
		if ((order.getStatus().equals(OrderStatus.STATUS_PENDING)
				&& status.equalsIgnoreCase(OrderStatus.STATUS_SHIPPING))
				|| (order.getStatus().equals(OrderStatus.STATUS_SHIPPING)
				&& status.equalsIgnoreCase(OrderStatus.STATUS_PENDING))) {

			order.setStatus(status.toUpperCase());
		}
		log.info("Admin id: {} changed order id: {} status {} to status {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), order.getId(), beforeStatus, status.toUpperCase());
		orderRepository.save(order);
		return orderMapper.mapToOrderDTO(order);
	}

	/** Delete a order. **/
	@Transactional
	@Override
	public void deleteOrder(UUID id) {
		Order order = orderRepository.findById(id).orElse(null);
		if (order == null) {
			throw new NotFoundOrderException("Not found order id: " + id);
		}
		if (order.getStatus().equals(OrderStatus.STATUS_SUCCESS)) {
			throw new CannotDeleteSuccessOrderException("Cannot delete success order");
		}
		if (order.getStatus().equals(OrderStatus.STATUS_SHIPPING)) {
			throw new CannotDeleteShippingOrderException("Cannot delete shipping order");
		}
		if (order.getStatus().equals(OrderStatus.STATUS_PENDING)) {
			List<Orderdetail> orderdetails = orderdetailRepository.findByOrderId(order.getId());
			orderdetails.forEach(od -> {
				Optional<Book> check = orderdetailRepository.findBookByOrderdetailId(od.getId());
				if (check.isEmpty()) {
					throw new NotFoundBookException("Not found book with orderdetail id: " + od.getId());
				}
				Book book = check.get();
				book.setAvailableQuantity(book.getAvailableQuantity() + od.getQuantityOrdered());
				bookRepository.save(book);
			});
		}
		orderRepository.delete(order);
	}
}
