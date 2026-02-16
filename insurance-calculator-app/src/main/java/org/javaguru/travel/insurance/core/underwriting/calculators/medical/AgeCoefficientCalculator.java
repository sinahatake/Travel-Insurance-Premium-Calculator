package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.domain.AgeCoefficient;
import org.javaguru.travel.insurance.core.repositories.AgeCoefficientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class AgeCoefficientCalculator {
    private final AgeCoefficientRepository ageCoefficientRepository;

    @Value("${age.coefficient.enabled}")
    private boolean ageCoefficientEnabled;

    BigDecimal calculate(PersonDTO person) {
        return ageCoefficientEnabled
                ? getCoefficient(person)
                : getDefaultValue();
    }


    private BigDecimal getCoefficient(PersonDTO person) {
        int age = Period.between(person.getPersonBirthDate(), LocalDate.now()).getYears();
        return ageCoefficientRepository.findCoefficientByAge(age)
                .map(AgeCoefficient::getCoefficient)
                .orElseThrow(() -> new RuntimeException("Age not found: " + age));
    }

    private BigDecimal getDefaultValue() {
        return BigDecimal.ONE;
    }

}
