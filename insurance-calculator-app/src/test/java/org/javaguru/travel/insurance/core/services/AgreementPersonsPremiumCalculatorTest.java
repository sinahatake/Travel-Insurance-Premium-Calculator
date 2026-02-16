package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
import org.javaguru.travel.insurance.core.underwriting.TravelPremiumCalculationResult;
import org.javaguru.travel.insurance.core.underwriting.TravelPremiumUnderwriting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgreementPersonsPremiumCalculatorTest {

    @Mock
    private TravelPremiumUnderwriting premiumUnderwriting;

    @InjectMocks
    private AgreementPersonsPremiumCalculator calculator;

    @Test
    void shouldCalculateAndSetRiskPremiumsForAllPersons() {
        PersonDTO person1 = new PersonDTO();
        PersonDTO person2 = new PersonDTO();
        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of(person1, person2));

        List<RiskDTO> risks1 = List.of(new RiskDTO("RISK1", BigDecimal.TEN));
        List<RiskDTO> risks2 = List.of(new RiskDTO("RISK2", BigDecimal.ONE));

        when(premiumUnderwriting.calculatePremium(agreement, person1))
                .thenReturn(new TravelPremiumCalculationResult(BigDecimal.TEN, risks1));
        when(premiumUnderwriting.calculatePremium(agreement, person2))
                .thenReturn(new TravelPremiumCalculationResult(BigDecimal.ONE, risks2));

        calculator.calculateRiskPremiums(agreement);

        assertEquals(risks1, person1.getRisks());
        assertEquals(risks2, person2.getRisks());

        verify(premiumUnderwriting).calculatePremium(agreement, person1);
        verify(premiumUnderwriting).calculatePremium(agreement, person2);
    }

    @Test
    void shouldHandleEmptyPersonList() {
        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(Collections.emptyList());

        calculator.calculateRiskPremiums(agreement);

        verifyNoInteractions(premiumUnderwriting);
    }

    @Test
    void shouldHandleOnePersonWithNoRisks() {
        PersonDTO person = new PersonDTO();
        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of(person));

        when(premiumUnderwriting.calculatePremium(agreement, person))
                .thenReturn(new TravelPremiumCalculationResult(BigDecimal.ZERO, Collections.emptyList()));

        calculator.calculateRiskPremiums(agreement);

        assertEquals(Collections.emptyList(), person.getRisks());
        verify(premiumUnderwriting).calculatePremium(agreement, person);
    }


}
