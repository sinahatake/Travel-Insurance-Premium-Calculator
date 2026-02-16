package org.javaguru.travel.insurance.core.underwriting;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelPremiumUnderwritingImplTest {

    @Mock
    private SelectedRisksPremiumCalculator selectedRisksPremiumCalculator;

    @InjectMocks
    private TravelPremiumUnderwritingImpl underwriting;

    @Test
    void shouldCalculateTotalPremiumAsSumOfRiskPremiums() {
        AgreementDTO agreement = mock(AgreementDTO.class);
        PersonDTO person = mock(PersonDTO.class);

        List<RiskDTO> risks = List.of(
                new RiskDTO("TRAVEL_MEDICAL", BigDecimal.ONE),
                new RiskDTO("TRAVEL_EVACUATION", BigDecimal.ONE)
        );

        when(selectedRisksPremiumCalculator.calculatePremiumForAllRisks(agreement, person)).thenReturn(risks);

        TravelPremiumCalculationResult result = underwriting.calculatePremium(agreement, person);

        assertEquals(new BigDecimal("2"), result.totalPremium());
        assertEquals(risks, result.risks());
    }

    @Test
    void shouldReturnZeroWhenNoRisksSelected() {
        AgreementDTO agreement = mock(AgreementDTO.class);
        PersonDTO person = mock(PersonDTO.class);

        when(selectedRisksPremiumCalculator.calculatePremiumForAllRisks(agreement, person)).thenReturn(List.of());

        TravelPremiumCalculationResult result = underwriting.calculatePremium(agreement, person);

        assertEquals(BigDecimal.ZERO, result.totalPremium());
        assertEquals(List.of(), result.risks());
    }
}
