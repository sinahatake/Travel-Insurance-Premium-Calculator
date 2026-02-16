package org.javaguru.travel.insurance.core.validations.person;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class ValidationDateOfBirthNotNull extends TravelPersonFieldValidationImpl {
    private final ValidationErrorFactory errorFactory;

    @Value("${age.coefficient.enabled}")
    private boolean ageCoefficientEnabled;

    @Override
    public Optional<ValidationErrorDTO> validate(PersonDTO personDTO) {
        return (personDTO.getPersonBirthDate() == null &&
                isAgeCoefficientEnabled(ageCoefficientEnabled))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_12"))
                : Optional.empty();
    }

    private boolean isAgeCoefficientEnabled(boolean ageCoefficientEnabled) {
        return ageCoefficientEnabled;
    }
}
