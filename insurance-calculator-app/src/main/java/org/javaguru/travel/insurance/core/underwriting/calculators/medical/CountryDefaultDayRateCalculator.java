package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.domain.CountryDefaultDayRate;
import org.javaguru.travel.insurance.core.repositories.CountryDefaultDayRateRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class CountryDefaultDayRateCalculator {
    private final CountryDefaultDayRateRepository countryDefaultDayRaterepository;

    BigDecimal calculate(AgreementDTO agreement) {
        return countryDefaultDayRaterepository.findByCountryIc(agreement.getCountry())
                .map(CountryDefaultDayRate::getDefaultDayRate)
                .orElseThrow(() -> new RuntimeException("Country not found: " + agreement.getCountry()));
    }
}
