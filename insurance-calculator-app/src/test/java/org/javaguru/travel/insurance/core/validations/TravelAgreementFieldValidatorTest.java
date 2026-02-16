package org.javaguru.travel.insurance.core.validations;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TravelAgreementFieldValidatorTest {

    private TravelAgreementFieldValidation validation1;
    private TravelAgreementFieldValidation validation2;
    private TravelAgreementFieldValidator validator;

    @BeforeEach
    void setup() {
        validation1 = mock(TravelAgreementFieldValidation.class);
        validation2 = mock(TravelAgreementFieldValidation.class);
        validator = new TravelAgreementFieldValidator(List.of(validation1, validation2));
    }

    @Test
    void shouldReturnErrorsFromSingleAndListValidations() {
        // given
        AgreementDTO agreement = new AgreementDTO();

        ValidationErrorDTO singleError = new ValidationErrorDTO("field1", "must not be null");
        ValidationErrorDTO listError = new ValidationErrorDTO("field2", "must have at least one person");

        when(validation1.validate(agreement)).thenReturn(Optional.of(singleError));
        when(validation2.validate(agreement)).thenReturn(Optional.empty());

        when(validation1.validateList(agreement)).thenReturn(List.of());
        when(validation2.validateList(agreement)).thenReturn(List.of(listError));

        // when
        List<ValidationErrorDTO> result = validator.validate(agreement);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(singleError));
        assertTrue(result.contains(listError));
    }

    @Test
    void shouldReturnEmptyListIfNoErrors() {
        // given
        AgreementDTO agreement = new AgreementDTO();

        when(validation1.validate(agreement)).thenReturn(Optional.empty());
        when(validation2.validate(agreement)).thenReturn(Optional.empty());

        when(validation1.validateList(agreement)).thenReturn(List.of());
        when(validation2.validateList(agreement)).thenReturn(List.of());

        // when
        List<ValidationErrorDTO> result = validator.validate(agreement);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCollectMultipleListErrors() {
        // given
        AgreementDTO agreement = new AgreementDTO();

        ValidationErrorDTO listError1 = new ValidationErrorDTO("field1", "error1");
        ValidationErrorDTO listError2 = new ValidationErrorDTO("field2", "error2");

        when(validation1.validate(agreement)).thenReturn(Optional.empty());
        when(validation2.validate(agreement)).thenReturn(Optional.empty());

        when(validation1.validateList(agreement)).thenReturn(List.of(listError1));
        when(validation2.validateList(agreement)).thenReturn(List.of(listError2));

        // when
        List<ValidationErrorDTO> result = validator.validate(agreement);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(listError1));
        assertTrue(result.contains(listError2));
    }
}
