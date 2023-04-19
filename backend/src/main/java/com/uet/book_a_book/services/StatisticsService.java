package com.uet.book_a_book.services;

import com.uet.book_a_book.dtos.Statistics;

public interface StatisticsService {
    Statistics getStatistics(Integer year, Integer month);
}
