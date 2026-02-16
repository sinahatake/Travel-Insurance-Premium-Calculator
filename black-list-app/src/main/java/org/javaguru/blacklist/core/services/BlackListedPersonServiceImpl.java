package org.javaguru.blacklist.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.blacklist.core.api.command.BlackListedPersonCoreCommand;
import org.javaguru.blacklist.core.api.command.BlackListedPersonCoreResult;
import org.javaguru.blacklist.core.api.dto.BlackListedPersonDTO;
import org.javaguru.blacklist.core.api.dto.ValidationErrorDTO;
import org.javaguru.blacklist.core.repositories.BlackListedPersonEntityRepository;
import org.javaguru.blacklist.core.validations.BlackListRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class BlackListedPersonServiceImpl implements BlackListedPersonService {

    private final BlackListedPersonEntityRepository repository;
    private final BlackListRequestValidator validator;

    @Override
    public BlackListedPersonCoreResult check(BlackListedPersonCoreCommand command) {
        BlackListedPersonDTO person = command.getPersonDTO();
        List<ValidationErrorDTO> errors = validator.validate(person);

        if (!errors.isEmpty()) {
            return buildErrorResponse(errors);
        }

        Boolean blackListed = repository
                .findByFirstNameAndLastNameAndPersonCode(
                        person.getPersonFirstName(),
                        person.getPersonLastName(),
                        person.getPersonCode()
                )
                .isPresent();

        person.setBlacklisted(blackListed);
        return buildSuccessResponse(person);
    }

    private BlackListedPersonCoreResult buildErrorResponse(List<ValidationErrorDTO> errors) {
        return new BlackListedPersonCoreResult(errors);
    }

    private BlackListedPersonCoreResult buildSuccessResponse(BlackListedPersonDTO person) {
        return new BlackListedPersonCoreResult(null, person);
    }
}
