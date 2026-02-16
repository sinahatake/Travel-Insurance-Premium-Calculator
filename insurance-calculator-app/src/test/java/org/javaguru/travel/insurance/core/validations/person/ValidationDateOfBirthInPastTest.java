package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationDateOfBirthInPastTest {

    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private ValidationDateOfBirthInPast validation;

    @Test
    public void shouldReturnErrorWhenDateOfBirthIsInFuture() {
        ReflectionTestUtils.setField(validation, "ageCoefficientEnabled", true);
        PersonDTO request = mock(PersonDTO.class);
        when(request.getPersonBirthDate()).thenReturn(LocalDate.now().plusDays(1));

        ValidationErrorDTO ValidationErrorDTO = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_13")).thenReturn(ValidationErrorDTO);

        Optional<ValidationErrorDTO> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertSame(ValidationErrorDTO, errorOpt.get());
    }

    @Test
    public void shouldNotReturnErrorWhenDateOfBirthIsInPast() {
        ReflectionTestUtils.setField(validation, "ageCoefficientEnabled", true);
        PersonDTO request = mock(PersonDTO.class);
        when(request.getPersonBirthDate()).thenReturn(LocalDate.now().minusYears(20));

        Optional<ValidationErrorDTO> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isEmpty());
        verifyNoInteractions(errorFactory);
    }

    @Test
    public void shouldNotReturnErrorWhenDateOfBirthIsNull() {
        PersonDTO request = mock(PersonDTO.class);
        when(request.getPersonBirthDate()).thenReturn(null);

        Optional<ValidationErrorDTO> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isEmpty());
        verifyNoInteractions(errorFactory);
    }
}
