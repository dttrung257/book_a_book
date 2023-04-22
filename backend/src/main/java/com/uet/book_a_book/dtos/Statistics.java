package com.uet.book_a_book.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Statistics {
    private final Long booksSold;
    private final Double revenue;
    private final Integer successfulOrders;
    private final Integer canceledOrders;
    private final Integer pendingOrders;
    private final Integer shippingOrders;
    private final Integer year;
    private final Integer month;
}
