package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelMedicalRiskPremiumCalculatorTest {

    @Mock
    private AgeCoefficientCalculator ageCoefficientCalculator;

    @Mock
    private CountryDefaultDayRateCalculator countryDefaultDayRateCalculator;

    @Mock
    private DayCountCalculator dayCountCalculator;

    @Mock
    private InsuranceLimitCoefficientCalculator insuranceLimitCoefficientCalculator;

    @InjectMocks
    private TravelMedicalRiskPremiumCalculator calculator;

    private AgreementDTO agreement;
    private PersonDTO person;

    @BeforeEach
    void setUp() {
        agreement = new AgreementDTO();
        person = new PersonDTO();
    }

    @Test
    @DisplayName("Should correctly calculate premium")
    void shouldCalculatePremium() {
        when(dayCountCalculator.calculate(agreement)).thenReturn(new BigDecimal("5"));
        when(countryDefaultDayRateCalculator.calculate(agreement)).thenReturn(new BigDecimal("10"));
        when(ageCoefficientCalculator.calculate(person)).thenReturn(new BigDecimal("1.5"));
        when(insuranceLimitCoefficientCalculator.calculate(person)).thenReturn(new BigDecimal("1"));

        BigDecimal premium = calculator.calculatePremium(agreement, person);

        assertEquals(new BigDecimal("75.00"), premium);
    }

    @Test
    @DisplayName("getRiskIc should return correct risk identifier")
    void shouldReturnCorrectRiskIc() {
        assertEquals("TRAVEL_MEDICAL", calculator.getRiskIc());
    }
}
