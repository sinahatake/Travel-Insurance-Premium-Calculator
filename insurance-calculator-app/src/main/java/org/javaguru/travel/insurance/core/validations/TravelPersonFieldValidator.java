package org.javaguru.travel.insurance.core.validations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class TravelPersonFieldValidator {

    private final List<TravelPersonFieldValidation> travelPersonFieldValidations;

    public List<ValidationErrorDTO> validate(AgreementDTO agreementDTO) {
        return agreementDTO.getPersons().stream()
                .map(this::collectOnePersonErrors)
                .flatMap(List::stream)
                .toList();
    }

    private List<ValidationErrorDTO> collectOnePersonErrors(PersonDTO personDTO) {
        List<ValidationErrorDTO> singleErrors = collectPersonSingleErrors(personDTO);
        List<ValidationErrorDTO> listErrors = collectPersonListErrors(personDTO);
        return concatenateLists(singleErrors, listErrors);
    }

    private List<ValidationErrorDTO> collectPersonSingleErrors(PersonDTO personDTO) {
        return travelPersonFieldValidations.stream()
                .map(validation -> validation.validate(personDTO))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private List<ValidationErrorDTO> collectPersonListErrors(PersonDTO personDTO) {
        return travelPersonFieldValidations.stream()
                .map(validation -> validation.validateList(personDTO))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<ValidationErrorDTO> concatenateLists(List<ValidationErrorDTO> singleErrors,
                                                      List<ValidationErrorDTO> listErrors) {
        return Stream.concat(singleErrors.stream(), listErrors.stream())
                .toList();
    }
}
