package com.uet.book_a_book.controllers;

import com.uet.book_a_book.services.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales_stats")
@Validated
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> getStatistics(
            @RequestParam(required = false) @Min(value = 1900) @Max(value = 9999) Integer year,
            @RequestParam(required = false) @Min(value = 1) @Max(value = 12) Integer month,
            @RequestParam(required = false) @Min(value = 1) @Max(value = 12) Integer range
    ) {
        if (Optional.ofNullable(range).isPresent() && Optional.ofNullable(month).isEmpty()) {
            return new ResponseEntity<>("month is required", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(statisticsService.getStatistics(year, month, range));
    }
}
