package org.javaguru.travel.insurance.core.validations.person;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
class ValidationPersonalCodeNotEmpty extends TravelPersonFieldValidationImpl {
    private final ValidationErrorFactory errorFactory;

    @Override
    public Optional<ValidationErrorDTO> validate(PersonDTO personDTO) {
        return (personDTO.getPersonCode() == null || personDTO.getPersonCode().isBlank())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_16"))
                : Optional.empty();
    }

}
