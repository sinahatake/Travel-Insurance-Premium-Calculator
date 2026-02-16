
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
class ValidationLastNameFormatTest {

    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private ValidationLastNameFormat validation;

    @Test
    void shouldReturnErrorWhenLastNameIsInvalid() {
        PersonDTO person = mock(PersonDTO.class);
        when(person.getPersonLastName()).thenReturn("Doe123"); // неправильный формат

        ValidationErrorDTO errorDTO = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_22")).thenReturn(errorDTO);

        Optional<ValidationErrorDTO> errorOpt = validation.validate(person);

        assertTrue(errorOpt.isPresent());
        assertSame(errorDTO, errorOpt.get());
    }

    @Test
    void shouldNotReturnErrorWhenLastNameIsValid() {
        PersonDTO person = mock(PersonDTO.class);
        when(person.getPersonLastName()).thenReturn("Smith"); // правильный формат

        Optional<ValidationErrorDTO> errorOpt = validation.validate(person);

        assertTrue(errorOpt.isEmpty());
        verifyNoInteractions(errorFactory);
    }

}
