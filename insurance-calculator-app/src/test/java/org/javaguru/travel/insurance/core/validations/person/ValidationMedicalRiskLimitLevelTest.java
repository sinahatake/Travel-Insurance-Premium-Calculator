package org.javaguru.travel.insurance.core.validations.person;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.domain.ClassifierValue;
import org.javaguru.travel.insurance.core.repositories.ClassifierValueRepository;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ValidationMedicalRiskLimitLevelTest {

    private ValidationErrorFactory errorFactory;
    private ClassifierValueRepository classifierValueRepository;
    private ValidationMedicalRiskLimitLevel validation;

    @BeforeEach
    void setUp() {
        errorFactory = mock(ValidationErrorFactory.class);
        classifierValueRepository = mock(ClassifierValueRepository.class);
        validation = new ValidationMedicalRiskLimitLevel(errorFactory, classifierValueRepository);
    }

    @Test
    void shouldReturnEmpty_whenMedicalRiskLimitLevelIsNull() {
        PersonDTO request = mock(PersonDTO.class);
        when(request.getMedicalRiskLimitLevel()).thenReturn(null);

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmpty_whenMedicalRiskLimitLevelIsBlank() {
        PersonDTO request = mock(PersonDTO.class);
        when(request.getMedicalRiskLimitLevel()).thenReturn("   ");

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmpty_whenMedicalRiskLimitLevelExistsInDatabase() {
        PersonDTO request = mock(PersonDTO.class);
        when(request.getMedicalRiskLimitLevel()).thenReturn("LEVEL_10000");
        when(classifierValueRepository.findByClassifierTitleAndIc("MEDICAL_RISK_LIMIT_LEVEL", "LEVEL_10000"))
                .thenReturn(Optional.of(mock(ClassifierValue.class)));

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnError_whenMedicalRiskLimitLevelNotExistsInDatabase() {
        PersonDTO request = mock(PersonDTO.class);
        when(request.getMedicalRiskLimitLevel()).thenReturn("INVALID_LEVEL");
        when(classifierValueRepository.findByClassifierTitleAndIc("MEDICAL_RISK_LIMIT_LEVEL", "INVALID_LEVEL"))
                .thenReturn(Optional.empty());

        String expectedDescription = "MedicalRiskLimitLevel = INVALID_LEVEL not supported!";
        ValidationErrorDTO expectedError = new ValidationErrorDTO("ERROR_CODE_15", expectedDescription);

        when(errorFactory.buildError(eq("ERROR_CODE_15"), anyList())).thenReturn(expectedError);

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isPresent());
        assertEquals(expectedError, result.get());

        verify(classifierValueRepository).findByClassifierTitleAndIc("MEDICAL_RISK_LIMIT_LEVEL", "INVALID_LEVEL");
        verify(errorFactory).buildError(eq("ERROR_CODE_15"), anyList());
    }
}
