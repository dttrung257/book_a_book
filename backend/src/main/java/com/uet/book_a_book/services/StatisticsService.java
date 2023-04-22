package com.uet.book_a_book.services;

import com.uet.book_a_book.dtos.Statistics;

import java.util.List;

public interface StatisticsService {
    List<Statistics> getStatistics(Integer year, Integer month, Integer range);
}
