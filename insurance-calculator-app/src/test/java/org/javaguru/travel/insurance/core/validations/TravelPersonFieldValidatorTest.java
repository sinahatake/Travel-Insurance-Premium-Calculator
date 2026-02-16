package org.javaguru.travel.insurance.core.validations;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TravelPersonFieldValidatorTest {

    private TravelPersonFieldValidation validation1;
    private TravelPersonFieldValidation validation2;
    private TravelPersonFieldValidator validator;

    @BeforeEach
    void setup() {
        validation1 = mock(TravelPersonFieldValidation.class);
        validation2 = mock(TravelPersonFieldValidation.class);
        validator = new TravelPersonFieldValidator(List.of(validation1, validation2));
    }

    @Test
    void shouldReturnErrorsForOnePerson() {
        // given
        PersonDTO person = new PersonDTO();
        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of(person));

        ValidationErrorDTO error1 = new ValidationErrorDTO("field1", "must not be empty");
        ValidationErrorDTO error2 = new ValidationErrorDTO("field2", "must be a valid date");

        when(validation1.validate(person)).thenReturn(Optional.of(error1));
        when(validation2.validate(person)).thenReturn(Optional.empty());

        when(validation1.validateList(person)).thenReturn(List.of());
        when(validation2.validateList(person)).thenReturn(List.of(error2));

        List<ValidationErrorDTO> result = validator.validate(agreement);

        assertEquals(2, result.size());
        assertTrue(result.contains(error1));
        assertTrue(result.contains(error2));
    }

    @Test
    void shouldReturnErrorsForMultiplePersons() {
        PersonDTO person1 = new PersonDTO();
        PersonDTO person2 = new PersonDTO();
        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of(person1, person2));

        ValidationErrorDTO error1 = new ValidationErrorDTO("p1", "error1");
        ValidationErrorDTO error2 = new ValidationErrorDTO("p2", "error2");

        when(validation1.validate(person1)).thenReturn(Optional.of(error1));
        when(validation1.validate(person2)).thenReturn(Optional.empty());

        when(validation1.validateList(person1)).thenReturn(List.of());
        when(validation1.validateList(person2)).thenReturn(List.of(error2));

        when(validation2.validate(any())).thenReturn(Optional.empty());
        when(validation2.validateList(any())).thenReturn(List.of());


        List<ValidationErrorDTO> result = validator.validate(agreement);

        assertEquals(2, result.size());
        assertTrue(result.contains(error1));
        assertTrue(result.contains(error2));
    }

    @Test
    void shouldReturnEmptyListWhenNoErrors() {
        // given
        PersonDTO person = new PersonDTO();
        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of(person));

        when(validation1.validate(person)).thenReturn(Optional.empty());
        when(validation2.validate(person)).thenReturn(Optional.empty());

        when(validation1.validateList(person)).thenReturn(List.of());
        when(validation2.validateList(person)).thenReturn(List.of());

        // when
        List<ValidationErrorDTO> result = validator.validate(agreement);

        // then
        assertTrue(result.isEmpty());
    }
}
