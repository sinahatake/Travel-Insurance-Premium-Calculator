package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.util.DateTimeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DayCountCalculatorTest {

    @Mock
    private DateTimeUtil dateTimeUtil;

    @InjectMocks
    private DayCountCalculator calculator;

    private AgreementDTO agreement;

    @BeforeEach
    void setUp() {
        agreement = new AgreementDTO();
        agreement.setAgreementDateFrom(LocalDate.of(2025, 7, 1));
        agreement.setAgreementDateTo(LocalDate.of(2025, 7, 11));
    }

    @Test
    @DisplayName("Should calculate correct day count")
    void shouldCalculateDayCountCorrectly() {
        long expectedDays = 10L;
        when(dateTimeUtil.getDaysBetween(
                agreement.getAgreementDateFrom(),
                agreement.getAgreementDateTo()
        )).thenReturn(expectedDays);

        BigDecimal result = calculator.calculate(agreement);

        assertEquals(BigDecimal.valueOf(expectedDays), result);
    }
}
