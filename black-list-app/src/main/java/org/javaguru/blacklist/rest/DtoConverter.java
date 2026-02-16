package org.javaguru.blacklist.rest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.blacklist.core.api.command.BlackListedPersonCoreCommand;
import org.javaguru.blacklist.core.api.command.BlackListedPersonCoreResult;
import org.javaguru.blacklist.core.api.dto.BlackListedPersonDTO;
import org.javaguru.blacklist.core.api.dto.ValidationErrorDTO;
import org.javaguru.blacklist.dto.BlackListRequest;
import org.javaguru.blacklist.dto.BlackListResponse;
import org.javaguru.blacklist.dto.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DtoConverter {

    public BlackListedPersonCoreCommand buildCoreCommand(BlackListRequest request) {
        BlackListedPersonDTO person = buildPerson(request);
        return new BlackListedPersonCoreCommand(person);
    }

    public BlackListResponse buildResponse(BlackListedPersonCoreResult coreResult) {
        return coreResult.hasErrors()
                ? buildResponseWithErrors(coreResult.getErrors())
                : buildSuccessfulResponse(coreResult);
    }


    private BlackListedPersonDTO buildPerson(BlackListRequest request) {
        BlackListedPersonDTO person = new BlackListedPersonDTO();
        person.setPersonCode(request.getPersonCode());
        person.setPersonFirstName(request.getPersonFirstName());
        person.setPersonLastName(request.getPersonLastName());
        return person;
    }

    private BlackListResponse buildResponseWithErrors(List<ValidationErrorDTO> errors) {
        return new BlackListResponse(transformValidationErrors(errors));
    }

    private List<ValidationError> transformValidationErrors(List<ValidationErrorDTO> errors) {
        return errors.stream()
                .map(error -> new ValidationError(error.getErrorCode(), error.getDescription()))
                .toList();

    }

    private BlackListResponse buildSuccessfulResponse(BlackListedPersonCoreResult coreResult) {
        BlackListResponse response = new BlackListResponse();

        response.setPersonCode(coreResult.getPersonDTO().getPersonCode());
        response.setPersonFirstName(coreResult.getPersonDTO().getPersonFirstName());
        response.setPersonLastName(coreResult.getPersonDTO().getPersonLastName());
        response.setBlacklisted(coreResult.getPersonDTO().getBlacklisted());

        return response;

    }


}
