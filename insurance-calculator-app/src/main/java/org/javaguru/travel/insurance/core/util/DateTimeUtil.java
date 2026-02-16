package org.javaguru.travel.insurance.core.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;


@Component
public class DateTimeUtil {

    public long getDaysBetween(LocalDate from, LocalDate to) {
        return from.until(to, DAYS);
    }
}
