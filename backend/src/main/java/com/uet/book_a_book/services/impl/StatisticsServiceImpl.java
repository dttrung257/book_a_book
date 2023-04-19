package com.uet.book_a_book.services.impl;

import com.uet.book_a_book.dtos.Statistics;
import com.uet.book_a_book.models.constant.OrderStatus;
import com.uet.book_a_book.repositories.OrderRepository;
import com.uet.book_a_book.repositories.OrderdetailRepository;
import com.uet.book_a_book.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final OrderdetailRepository orderdetailRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    @Override
    public Statistics getStatistics(Integer year, Integer month) {
        final double revenue = orderdetailRepository.getRevenue(year, month).orElse(0.0);
        final long booksSold = orderdetailRepository.getBooksSold(year, month).orElse(0L);
        final int successfulOrders = orderRepository.getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_SUCCESS).orElse(0);
        final int canceledOrders = orderRepository.getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_CANCELED).orElse(0);
        final int pendingOrders = orderRepository.getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_PENDING).orElse(0);
        final int shippingOrders = orderRepository.getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_SHIPPING).orElse(0);
        return Statistics.builder()
                .booksSold(booksSold)
                .revenue(revenue)
                .successfulOrders(successfulOrders)
                .canceledOrders(canceledOrders)
                .pendingOrders(pendingOrders)
                .shippingOrders(shippingOrders)
                .build();
    }
}
