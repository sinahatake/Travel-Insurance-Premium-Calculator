package org.javaguru.travel.insurance.core.underwriting;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
class TravelPremiumUnderwritingImpl implements TravelPremiumUnderwriting {

    private final SelectedRisksPremiumCalculator selectedRisksPremiumCalculator;

    @Override
    public TravelPremiumCalculationResult calculatePremium(AgreementDTO agreement, PersonDTO person) {
        List<RiskDTO> selectedRiskDTO = selectedRisksPremiumCalculator.calculatePremiumForAllRisks(agreement, person);
        BigDecimal totalPremium = calculateTotalPremium(selectedRiskDTO);

        return new TravelPremiumCalculationResult(totalPremium, selectedRiskDTO);
    }

    private static BigDecimal calculateTotalPremium(List<RiskDTO> RiskDTOs) {
        return RiskDTOs.stream()
                .map(RiskDTO::getPremium)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
