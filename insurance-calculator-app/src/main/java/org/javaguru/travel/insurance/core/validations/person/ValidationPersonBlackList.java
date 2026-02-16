package org.javaguru.travel.insurance.core.validations.person;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.blacklist.BlackListedPersonService;
import org.javaguru.travel.insurance.core.util.Placeholder;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ValidationPersonBlackList extends TravelPersonFieldValidationImpl {

    private final BlackListedPersonService blackListedPersonService;
    private final ValidationErrorFactory errorFactory;

    @Override
    public Optional<ValidationErrorDTO> validate(PersonDTO person) {
        return (!personFirstNameIsNullOrBlank(person)
                && !personLastNameIsNullOrBlank(person)
                && !personCodeIsNullOrBlank(person))
                ? personBlackListedCheck(person)
                : Optional.empty();
    }

    private boolean personCodeIsNullOrBlank(PersonDTO person) {
        return person.getPersonCode() == null || person.getPersonCode().isBlank();
    }

    private boolean personFirstNameIsNullOrBlank(PersonDTO person) {
        return person.getPersonFirstName() == null || person.getPersonFirstName().isBlank();
    }

    private boolean personLastNameIsNullOrBlank(PersonDTO person) {
        return person.getPersonLastName() == null || person.getPersonLastName().isBlank();
    }

    private Optional<ValidationErrorDTO> personBlackListedCheck(PersonDTO person) {
        try {
            if (blackListedPersonService.isBlacklisted(person)) {
                Placeholder placeholder = new Placeholder("PERSON_CODE", person.getPersonCode());
                return Optional.of(errorFactory.buildError("ERROR_CODE_24", List.of(placeholder)));
            }
        } catch (Throwable e) {
            log.error("BlackList call failed!", e);
            return Optional.of(errorFactory.buildError("ERROR_CODE_25"));
        }
        return Optional.empty();
    }

}
