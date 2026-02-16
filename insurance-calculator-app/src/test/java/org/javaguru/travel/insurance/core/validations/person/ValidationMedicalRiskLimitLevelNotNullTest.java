package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationMedicalRiskLimitLevelNotNullTest {

    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private ValidationMedicalRiskLimitLevelNotNull validation;

    private PersonDTO request;

    @BeforeEach
    void setUp() throws Exception {
        request = new PersonDTO();
        // Установка значения поля через рефлексию
        Field field = ValidationMedicalRiskLimitLevelNotNull.class.getDeclaredField("medicalRiskLimitEnabled");
        field.setAccessible(true);
        field.set(validation, true); // включаем проверку
    }

    @Test
    void shouldReturnError_whenMedicalRiskLimitLevelIsNull_andValidationEnabled() {
        request.setMedicalRiskLimitLevel(null);
        ValidationErrorDTO expectedError = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_14")).thenReturn(expectedError);

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isPresent());
        assertEquals(expectedError, result.get());
    }

    @Test
    void shouldReturnError_whenMedicalRiskLimitLevelIsBlank_andValidationEnabled() {
        request.setMedicalRiskLimitLevel("   ");
        ValidationErrorDTO expectedError = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_14")).thenReturn(expectedError);

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isPresent());
        assertEquals(expectedError, result.get());
    }

    @Test
    void shouldNotReturnError_whenMedicalRiskLimitLevelIsValid() {
        request.setMedicalRiskLimitLevel("LEVEL_10000");

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotReturnError_whenValidationIsDisabled() throws Exception {
        // отключаем проверку
        Field field = ValidationMedicalRiskLimitLevelNotNull.class.getDeclaredField("medicalRiskLimitEnabled");
        field.setAccessible(true);
        field.set(validation, false);

        request.setMedicalRiskLimitLevel(null);

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isEmpty());
    }
}
