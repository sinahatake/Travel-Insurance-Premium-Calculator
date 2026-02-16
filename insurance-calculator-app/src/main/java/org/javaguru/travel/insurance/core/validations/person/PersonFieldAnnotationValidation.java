package org.javaguru.travel.insurance.core.validations.person;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.util.Placeholder;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
class PersonFieldAnnotationValidation extends TravelPersonFieldValidationImpl {
    private final ValidationErrorFactory errorFactory;

    @Override
    public List<ValidationErrorDTO> validateList(PersonDTO personDTO) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PersonDTO>> violations = validator.validate(personDTO);
        if (!violations.isEmpty()) {
            List<ValidationErrorDTO> validationErrors = new ArrayList<>();
            violations.forEach(violation -> {
                String fieldName = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                validationErrors.add(buildValidationErrorDTO(fieldName, message));
            });

            return validationErrors;
        } else {
            return List.of();
        }

    }

    private ValidationErrorDTO buildValidationErrorDTO(String field, String message) {
        Placeholder placeholder1 = new Placeholder("FIELD_NAME", field);
        Placeholder placeholder2 = new Placeholder("FIELD_MESSAGE", message);

        return errorFactory.buildError("ERROR_CODE_23", List.of(placeholder1, placeholder2));
    }

}
