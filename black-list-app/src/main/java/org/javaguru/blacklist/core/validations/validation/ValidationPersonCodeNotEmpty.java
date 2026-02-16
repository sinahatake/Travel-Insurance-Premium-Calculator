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
class ValidationPersonCodeNotEmpty implements BlackListRequestValidation {
    private final ValidationErrorFactory errorFactory;

    @Override
    public Optional<ValidationErrorDTO> validate(BlackListedPersonDTO personDTO) {
        return (personDTO.getPersonCode() == null || personDTO.getPersonCode().isBlank())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_3"))
                : Optional.empty();
    }

}
