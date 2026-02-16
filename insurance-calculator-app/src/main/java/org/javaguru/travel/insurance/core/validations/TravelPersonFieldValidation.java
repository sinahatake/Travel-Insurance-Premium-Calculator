package org.javaguru.travel.insurance.core.validations;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;

import java.util.List;
import java.util.Optional;

public interface TravelPersonFieldValidation {
    Optional<ValidationErrorDTO> validate(PersonDTO personDTO);

    List<ValidationErrorDTO> validateList(PersonDTO personDTO);
}
