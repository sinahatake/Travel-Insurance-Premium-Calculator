package org.javaguru.travel.insurance.core.validations.person;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class ValidationMedicalRiskLimitLevelNotNull extends TravelPersonFieldValidationImpl {

    private final ValidationErrorFactory errorFactory;

    @Value("${medical.risk.limit.level.enabled}")
    private boolean medicalRiskLimitEnabled;

    @Override
    public Optional<ValidationErrorDTO> validate(PersonDTO person) {
        return (isMedicalRiskLimitEnabled(medicalRiskLimitEnabled) &&
                !isMedicalRiskLimitLevelNotNull(person))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_14"))
                : Optional.empty();
    }

    private boolean isMedicalRiskLimitEnabled(boolean medicalRiskLimitEnabled) {
        return medicalRiskLimitEnabled;
    }

    private boolean isMedicalRiskLimitLevelNotNull(PersonDTO person) {
        return person.getMedicalRiskLimitLevel() != null && !person.getMedicalRiskLimitLevel().isBlank();
    }

}

