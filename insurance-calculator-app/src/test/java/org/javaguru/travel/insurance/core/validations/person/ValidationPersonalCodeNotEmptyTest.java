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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationPersonalCodeNotEmptyTest {

    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private ValidationPersonalCodeNotEmpty validation;

    @Test
    void shouldReturnErrorWhenPersonCodeIsNull() {
        PersonDTO person = mock(PersonDTO.class);
        when(person.getPersonCode()).thenReturn(null);

        ValidationErrorDTO errorDTO = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_16")).thenReturn(errorDTO);

        Optional<ValidationErrorDTO> errorOpt = validation.validate(person);

        assertTrue(errorOpt.isPresent());
        assertSame(errorDTO, errorOpt.get());
    }

    @Test
    void shouldReturnErrorWhenPersonCodeIsBlank() {
        PersonDTO person = mock(PersonDTO.class);
        when(person.getPersonCode()).thenReturn("   ");

        ValidationErrorDTO errorDTO = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_16")).thenReturn(errorDTO);

        Optional<ValidationErrorDTO> errorOpt = validation.validate(person);

        assertTrue(errorOpt.isPresent());
        assertSame(errorDTO, errorOpt.get());
    }

    @Test
    void shouldNotReturnErrorWhenPersonCodeIsNotBlank() {
        PersonDTO person = mock(PersonDTO.class);
        when(person.getPersonCode()).thenReturn("ABC123");

        Optional<ValidationErrorDTO> errorOpt = validation.validate(person);

        assertTrue(errorOpt.isEmpty());
        verifyNoInteractions(errorFactory);
    }
}
