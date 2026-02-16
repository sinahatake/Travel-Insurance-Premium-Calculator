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
class ValidationFirstNameFormatTest {

    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private ValidationFirstNameFormat validation;

    @Test
    void shouldReturnErrorWhenFirstNameIsInvalid() {
        PersonDTO request = mock(PersonDTO.class);
        when(request.getPersonFirstName()).thenReturn("паиуая");

        ValidationErrorDTO ValidationErrorDTO = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_21")).thenReturn(ValidationErrorDTO);

        Optional<ValidationErrorDTO> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertSame(errorOpt.get(), ValidationErrorDTO);
    }

    @Test
    void shouldNotReturnErrorWhenFirstNameIsValid() {
        PersonDTO person = mock(PersonDTO.class);
        when(person.getPersonFirstName()).thenReturn("Mary-Jane"); // правильный формат

        Optional<ValidationErrorDTO> errorOpt = validation.validate(person);

        assertTrue(errorOpt.isEmpty());
        verifyNoInteractions(errorFactory);
    }

}
