package org.javaguru.blacklist.core.validations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.blacklist.core.api.dto.BlackListedPersonDTO;
import org.javaguru.blacklist.core.api.dto.ValidationErrorDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class BlackListRequestValidatorImpl implements BlackListRequestValidator {

    private final List<BlackListRequestValidation> blackListRequestValidations;

    public List<ValidationErrorDTO> validate(BlackListedPersonDTO personDTO) {
        return blackListRequestValidations.stream()
                .map(validation -> validation.validate(personDTO))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

}
