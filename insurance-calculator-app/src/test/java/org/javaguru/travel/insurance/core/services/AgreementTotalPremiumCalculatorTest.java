package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AgreementTotalPremiumCalculatorTest {

    private final AgreementTotalPremiumCalculator calculator = new AgreementTotalPremiumCalculator();

    @Test
    void shouldReturnZeroWhenNoPersons() {
        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of());

        BigDecimal result = calculator.calculate(agreement);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldReturnZeroWhenPersonsHaveNoRisks() {
        PersonDTO person1 = new PersonDTO();
        person1.setRisks(List.of());

        PersonDTO person2 = new PersonDTO();
        person2.setRisks(List.of());

        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of(person1, person2));

        BigDecimal result = calculator.calculate(agreement);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldSumPremiumsForAllPersons() {
        PersonDTO person1 = new PersonDTO();
        person1.setRisks(List.of(
                new RiskDTO("R1", new BigDecimal("10.00")),
                new RiskDTO("R2", new BigDecimal("20.00"))
        ));

        PersonDTO person2 = new PersonDTO();
        person2.setRisks(List.of(
                new RiskDTO("R3", new BigDecimal("5.50"))
        ));

        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of(person1, person2));

        BigDecimal result = calculator.calculate(agreement);

        assertEquals(new BigDecimal("35.50"), result);
    }
}
