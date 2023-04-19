package com.uet.book_a_book.controllers;

import com.uet.book_a_book.dtos.Statistics;
import com.uet.book_a_book.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/sales_stats")
@Validated
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping
    public ResponseEntity<Statistics> getStatistics(
            @RequestParam(required = false) @Min(value = 1900) @Max(value = 9999) Integer year,
            @RequestParam(required = false) @Min(value = 1) @Max(value = 12) Integer month
    ) {
        return ResponseEntity.ok(statisticsService.getStatistics(year, month));
    }
}
