package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PersonFieldAnnotationValidationTest {

    private ValidationErrorFactory errorFactory;
    private PersonFieldAnnotationValidation validation;

    @BeforeEach
    void setUp() {
        errorFactory = mock(ValidationErrorFactory.class);
        validation = new PersonFieldAnnotationValidation(errorFactory);
    }

    @Test
    void shouldReturnErrorWhenFirstNameExceedsMaxLength() {
        PersonDTO person = new PersonDTO();
        person.setPersonFirstName("A".repeat(201));

        ValidationErrorDTO fakeError = new ValidationErrorDTO("ERROR_CODE_23", "field error");
        when(errorFactory.buildError(any(), any())).thenReturn(fakeError);

        List<ValidationErrorDTO> errors = validation.validateList(person);

        assertThat(errors).hasSize(1);
        verify(errorFactory).buildError(eq("ERROR_CODE_23"), anyList());
    }

    @Test
    void shouldReturnErrorWhenLastNameExceedsMaxLength() {
        PersonDTO person = new PersonDTO();
        person.setPersonLastName("A".repeat(201));

        ValidationErrorDTO fakeError = new ValidationErrorDTO("ERROR_CODE_23", "field error");
        when(errorFactory.buildError(any(), any())).thenReturn(fakeError);

        List<ValidationErrorDTO> errors = validation.validateList(person);

        assertThat(errors).hasSize(1);
        verify(errorFactory).buildError(eq("ERROR_CODE_23"), anyList());
    }

    @Test
    void shouldReturnNoErrorsWhenFirstNameIsValid() {
        PersonDTO person = new PersonDTO();
        // валидное имя ≤ 200 символов
        person.setPersonFirstName("John");
        person.setPersonLastName("John");

        List<ValidationErrorDTO> errors = validation.validateList(person);

        assertThat(errors).isEmpty();
        verifyNoInteractions(errorFactory);
    }
}
