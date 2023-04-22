package com.uet.book_a_book.services.impl;

import com.uet.book_a_book.dtos.Statistics;
import com.uet.book_a_book.models.constant.OrderStatus;
import com.uet.book_a_book.repositories.OrderRepository;
import com.uet.book_a_book.repositories.OrderdetailRepository;
import com.uet.book_a_book.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final OrderdetailRepository orderdetailRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Statistics> getStatistics(Integer year, Integer month, Integer range) {
        if (Optional.ofNullable(range).isEmpty()) {
            final double revenue = orderdetailRepository.getRevenue(year, month).orElse(0.0);
            final long booksSold = orderdetailRepository.getBooksSold(year, month).orElse(0L);
            final int successfulOrders = orderRepository
                    .getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_SUCCESS)
                    .orElse(0);
            final int canceledOrders = orderRepository
                    .getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_CANCELED)
                    .orElse(0);
            final int pendingOrders = orderRepository
                    .getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_PENDING)
                    .orElse(0);
            final int shippingOrders = orderRepository
                    .getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_SHIPPING)
                    .orElse(0);
            return List.of(Statistics.builder()
                    .booksSold(booksSold)
                    .revenue(revenue)
                    .successfulOrders(successfulOrders)
                    .canceledOrders(canceledOrders)
                    .pendingOrders(pendingOrders)
                    .shippingOrders(shippingOrders)
                    .year(year)
                    .month(month)
                    .build());
        } else {
            if (Optional.ofNullable(year).isEmpty()) {
                year = LocalDate.now().getYear();
            }
            final int lastMonth = Math.min(month + range - 1, 12);
            List<Statistics> statisticsList = new ArrayList<>();
            while (month <= lastMonth) {
                final double revenue = orderdetailRepository.getRevenue(year, month).orElse(0.0);
                final long booksSold = orderdetailRepository.getBooksSold(year, month).orElse(0L);
                final int successfulOrders = orderRepository
                        .getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_SUCCESS)
                        .orElse(0);
                final int canceledOrders = orderRepository
                        .getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_CANCELED)
                        .orElse(0);
                final int pendingOrders = orderRepository
                        .getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_PENDING)
                        .orElse(0);
                final int shippingOrders = orderRepository
                        .getNumberOfOrdersByStatus(year, month, OrderStatus.STATUS_SHIPPING)
                        .orElse(0);
                Statistics statistics = Statistics.builder()
                        .booksSold(booksSold)
                        .revenue(revenue)
                        .successfulOrders(successfulOrders)
                        .canceledOrders(canceledOrders)
                        .pendingOrders(pendingOrders)
                        .shippingOrders(shippingOrders)
                        .year(year)
                        .month(month)
                        .build();
                statisticsList.add(statistics);
                month++;
            }
            return statisticsList;
        }
    }
}
