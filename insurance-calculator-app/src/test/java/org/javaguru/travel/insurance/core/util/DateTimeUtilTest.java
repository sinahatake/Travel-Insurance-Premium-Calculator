package org.javaguru.travel.insurance.core.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateTimeUtilTest {
    private final DateTimeUtil dateTimeUtil = new DateTimeUtil();

    @Test
    void shouldCalculatePositive() {
        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 1, 10);
        assertEquals(9L, dateTimeUtil.getDaysBetween(dateFrom, dateTo));
    }

    @Test
    void shouldCalculate() {
        LocalDate dateFrom = LocalDate.of(2030, 1, 1);

        LocalDate dateTo = LocalDate.of(2030, 5, 1);
        assertEquals(120, dateTimeUtil.getDaysBetween(dateFrom, dateTo));
    }

    @Test
    void shouldCalculateNegative() {
        LocalDate dateFrom = LocalDate.of(2022, 1, 10);
        LocalDate dateTo = LocalDate.of(2022, 1, 1);
        assertEquals(-9L, dateTimeUtil.getDaysBetween(dateFrom, dateTo));
    }


    @Test
    void shouldCalculateZero() {
        LocalDate dateFrom = LocalDate.of(2022, 1, 10);
        LocalDate dateTo = LocalDate.of(2022, 1, 10);
        assertEquals(0L, dateTimeUtil.getDaysBetween(dateFrom, dateTo));
    }

}