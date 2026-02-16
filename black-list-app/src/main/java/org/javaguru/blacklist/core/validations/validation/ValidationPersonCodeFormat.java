package org.javaguru.blacklist.core.validations.validation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.javaguru.blacklist.core.api.dto.BlackListedPersonDTO;
import org.javaguru.blacklist.core.api.dto.ValidationErrorDTO;
import org.javaguru.blacklist.core.validations.BlackListRequestValidation;
import org.javaguru.blacklist.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
class ValidationPersonCodeFormat implements BlackListRequestValidation {
    private final ValidationErrorFactory errorFactory;

    @Override
    public Optional<ValidationErrorDTO> validate(BlackListedPersonDTO personDTO) {
        return (!isPersonalCodeIsNull(personDTO) && !personDTO.getPersonCode().matches("^\\d{6}-\\d{5}$"))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_4"))
                : Optional.empty();
    }

    private boolean isPersonalCodeIsNull(BlackListedPersonDTO person) {
        return person.getPersonCode() == null || person.getPersonCode().isBlank();
    }

}
