package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.domain.MedicalRiskLimitLevel;
import org.javaguru.travel.insurance.core.repositories.MedicalRiskLimitLevelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class InsuranceLimitCoefficientCalculator {
    private final MedicalRiskLimitLevelRepository repository;

    @Value("${medical.risk.limit.level.enabled}")
    private boolean medicalRiskLimitEnabled;

    BigDecimal calculate(PersonDTO person) {
        return medicalRiskLimitEnabled
                ? getCoefficient(person)
                : getDefaultValue();
    }


    private BigDecimal getCoefficient(PersonDTO person) {
        return repository.findByMedicalRiskLimitLevelIc(person.getMedicalRiskLimitLevel())
                .map(MedicalRiskLimitLevel::getCoefficient)
                .orElseThrow(() -> new RuntimeException("MedicalRiskLimitLevel not found: " + person.getMedicalRiskLimitLevel()));
    }

    private BigDecimal getDefaultValue() {
        return BigDecimal.ONE;
    }

}
