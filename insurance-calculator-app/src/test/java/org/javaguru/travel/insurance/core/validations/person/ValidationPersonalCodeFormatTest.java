package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationPersonalCodeFormatTest {

    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private ValidationPersonalCodeFormat validation;

    @Test
    void shouldReturnErrorWhenPersonCodeIsInvalid() {
        PersonDTO person = mock(PersonDTO.class);
        when(person.getPersonCode()).thenReturn("123");

        ValidationErrorDTO errorDTO = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_20")).thenReturn(errorDTO);

        Optional<ValidationErrorDTO> errorOpt = validation.validate(person);

        assertTrue(errorOpt.isPresent());
        assertSame(errorDTO, errorOpt.get());
    }

    @Test
    void shouldNotReturnErrorWhenPersonCodeIsValid() {
        PersonDTO person = mock(PersonDTO.class);
        when(person.getPersonCode()).thenReturn("010882-11034");

        Optional<ValidationErrorDTO> errorOpt = validation.validate(person);

        assertTrue(errorOpt.isEmpty());
        verifyNoInteractions(errorFactory);
    }

}
