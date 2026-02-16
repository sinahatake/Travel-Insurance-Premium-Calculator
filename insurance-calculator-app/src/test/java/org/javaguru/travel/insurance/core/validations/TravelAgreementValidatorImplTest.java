package org.javaguru.travel.insurance.core.validations;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TravelAgreementValidatorImplTest {

    private TravelAgreementFieldValidator agreementFieldValidator;
    private TravelPersonFieldValidator personFieldValidator;
    private TravelAgreementValidatorImpl validator;

    @BeforeEach
    void setUp() {
        agreementFieldValidator = mock(TravelAgreementFieldValidator.class);
        personFieldValidator = mock(TravelPersonFieldValidator.class);
        validator = new TravelAgreementValidatorImpl(agreementFieldValidator, personFieldValidator);
    }

    @Test
    void shouldReturnAllErrorsFromAgreementAndPersonValidators() {
        // given
        AgreementDTO agreement = new AgreementDTO();

        ValidationErrorDTO agreementError = new ValidationErrorDTO("field1", "error1");
        ValidationErrorDTO personError = new ValidationErrorDTO("person1", "error2");

        when(agreementFieldValidator.validate(agreement)).thenReturn(List.of(agreementError));
        when(personFieldValidator.validate(agreement)).thenReturn(List.of(personError));

        // when
        List<ValidationErrorDTO> result = validator.validate(agreement);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(agreementError));
        assertTrue(result.contains(personError));
    }

    @Test
    void shouldReturnOnlyAgreementErrorsIfNoPersonErrors() {
        AgreementDTO agreement = new AgreementDTO();
        ValidationErrorDTO agreementError = new ValidationErrorDTO("field1", "error1");

        when(agreementFieldValidator.validate(agreement)).thenReturn(List.of(agreementError));
        when(personFieldValidator.validate(agreement)).thenReturn(List.of());

        List<ValidationErrorDTO> result = validator.validate(agreement);

        assertEquals(1, result.size());
        assertEquals(agreementError, result.getFirst());
    }

    @Test
    void shouldReturnOnlyPersonErrorsIfNoAgreementErrors() {
        AgreementDTO agreement = new AgreementDTO();
        ValidationErrorDTO personError = new ValidationErrorDTO("field2", "error2");

        when(agreementFieldValidator.validate(agreement)).thenReturn(List.of());
        when(personFieldValidator.validate(agreement)).thenReturn(List.of(personError));

        List<ValidationErrorDTO> result = validator.validate(agreement);

        assertEquals(1, result.size());
        assertEquals(personError, result.getFirst());
    }

    @Test
    void shouldReturnEmptyListIfNoErrors() {
        AgreementDTO agreement = new AgreementDTO();

        when(agreementFieldValidator.validate(agreement)).thenReturn(List.of());
        when(personFieldValidator.validate(agreement)).thenReturn(List.of());

        List<ValidationErrorDTO> result = validator.validate(agreement);

        assertTrue(result.isEmpty());
    }
}
