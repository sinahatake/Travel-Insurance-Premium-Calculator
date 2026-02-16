package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.TravelPersonFieldValidation;

import java.util.List;
import java.util.Optional;

abstract public class TravelPersonFieldValidationImpl implements TravelPersonFieldValidation {
    @Override
    public Optional<ValidationErrorDTO> validate(PersonDTO personDTO) {
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDTO> validateList(PersonDTO personDTO) {
        return List.of();
    }

}