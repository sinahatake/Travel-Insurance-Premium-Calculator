package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.underwriting.TravelRiskPremiumCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class TravelMedicalRiskPremiumCalculator implements TravelRiskPremiumCalculator {
    private final AgeCoefficientCalculator ageCoefficientCalculator;
    private final CountryDefaultDayRateCalculator countryDefaultDayRateCalculator;
    private final DayCountCalculator dayCountCalculator;
    private final InsuranceLimitCoefficientCalculator insuranceLimitCoefficientCalculator;

    @Override
    public BigDecimal calculatePremium(AgreementDTO agreement, PersonDTO person) {
        var daysCount = dayCountCalculator.calculate(agreement);
        var countryDefaultRate = countryDefaultDayRateCalculator.calculate(agreement);
        var ageCoefficient = ageCoefficientCalculator.calculate(person);
        var insuranceLimitCoefficient = insuranceLimitCoefficientCalculator.calculate(person);
        return countryDefaultRate.
                multiply(daysCount).
                multiply(ageCoefficient).
                multiply(insuranceLimitCoefficient)
                .setScale(2, RoundingMode.HALF_UP);
    }


    @Override
    public String getRiskIc() {
        return "TRAVEL_MEDICAL";
    }
}
