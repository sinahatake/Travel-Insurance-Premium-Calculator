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
class ValidationPersonalCodeFormat extends TravelPersonFieldValidationImpl {
    private final ValidationErrorFactory errorFactory;

    @Override
    public Optional<ValidationErrorDTO> validate(PersonDTO personDTO) {
        return (!isPersonalCodeIsNull(personDTO) && !personDTO.getPersonCode().matches("^\\d{6}-\\d{5}$"))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_20"))
                : Optional.empty();
    }

    private boolean isPersonalCodeIsNull(PersonDTO person) {
        return person.getPersonCode() == null || person.getPersonCode().isBlank();
    }

}
