package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.domain.CountryDefaultDayRate;
import org.javaguru.travel.insurance.core.repositories.CountryDefaultDayRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryDefaultDayRateCalculatorTest {

    @Mock
    private CountryDefaultDayRateRepository repository;

    @InjectMocks
    private CountryDefaultDayRateCalculator calculator;

    private AgreementDTO agreement;

    @BeforeEach
    void setUp() {
        agreement = new AgreementDTO();
        agreement.setCountry("LATVIA");
    }

    @Test
    @DisplayName("Should return default day rate for existing country")
    void shouldReturnDefaultDayRate() {
        BigDecimal expectedRate = new BigDecimal("1.00");
        CountryDefaultDayRate entity = mock(CountryDefaultDayRate.class);
        when(entity.getDefaultDayRate()).thenReturn(expectedRate);

        when(repository.findByCountryIc("LATVIA")).thenReturn(Optional.of(entity));

        BigDecimal result = calculator.calculate(agreement);

        assertEquals(expectedRate, result);
    }

    @Test
    @DisplayName("Should throw exception if country not found")
    void shouldThrowExceptionIfCountryNotFound() {
        when(repository.findByCountryIc("LATVIA")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> calculator.calculate(agreement));

        assertEquals("Country not found: LATVIA", exception.getMessage());
    }
}
