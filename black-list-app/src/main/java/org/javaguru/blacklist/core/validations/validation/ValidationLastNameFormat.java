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
class ValidationLastNameFormat implements BlackListRequestValidation {
    private final ValidationErrorFactory errorFactory;

    @Override
    public Optional<ValidationErrorDTO> validate(BlackListedPersonDTO personDTO) {
        return (!isLastNameIsNull(personDTO) && !personDTO.getPersonLastName().matches("^[A-Za-z -]+$"))
                ? Optional.of(errorFactory.buildError("ERROR_CODE_6"))
                : Optional.empty();
    }

    private boolean isLastNameIsNull(BlackListedPersonDTO person) {
        return person.getPersonLastName() == null || person.getPersonLastName().isBlank();
    }
}

