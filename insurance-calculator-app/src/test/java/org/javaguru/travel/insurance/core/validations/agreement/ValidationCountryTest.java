package org.javaguru.travel.insurance.core.validations.agreement;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.domain.ClassifierValue;
import org.javaguru.travel.insurance.core.repositories.ClassifierValueRepository;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ValidationCountryTest {

    private ValidationErrorFactory errorFactory;
    private ClassifierValueRepository classifierValueRepository;
    private ValidationCountry validation;

    @BeforeEach
    void setUp() {
        errorFactory = mock(ValidationErrorFactory.class);
        classifierValueRepository = mock(ClassifierValueRepository.class);
        validation = new ValidationCountry(errorFactory, classifierValueRepository);
    }

    @Test
    void shouldReturnEmpty_whenSelectedRisksDoesNotContainTravelMedical() {
        AgreementDTO request = mock(AgreementDTO.class);
        when(request.getSelectedRisks()).thenReturn(List.of("TRAVEL_CANCELLATION"));
        when(request.getCountry()).thenReturn("UNKNOWN_COUNTRY");

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isEmpty());
        verifyNoInteractions(classifierValueRepository);
    }

    @Test
    void shouldReturnEmpty_whenCountryIsNull_butTravelMedicalSelected() {
        AgreementDTO request = mock(AgreementDTO.class);
        when(request.getSelectedRisks()).thenReturn(List.of("TRAVEL_MEDICAL"));
        when(request.getCountry()).thenReturn(null);

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isEmpty());
        verifyNoInteractions(classifierValueRepository);
    }

    @Test
    void shouldReturnEmpty_whenCountryExistsInDatabase() {
        AgreementDTO request = mock(AgreementDTO.class);
        when(request.getSelectedRisks()).thenReturn(List.of("TRAVEL_MEDICAL"));
        when(request.getCountry()).thenReturn("LATVIA");

        when(classifierValueRepository.findByClassifierTitleAndIc("COUNTRY", "LATVIA"))
                .thenReturn(Optional.of(mock(ClassifierValue.class)));

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isEmpty());
        verify(classifierValueRepository).findByClassifierTitleAndIc("COUNTRY", "LATVIA");
    }

    @Test
    void shouldReturnError_whenCountryNotExistsInDatabase_andTravelMedicalSelected() {
        AgreementDTO request = mock(AgreementDTO.class);
        when(request.getSelectedRisks()).thenReturn(List.of("TRAVEL_MEDICAL"));
        when(request.getCountry()).thenReturn("UNKNOWN_COUNTRY");

        when(classifierValueRepository.findByClassifierTitleAndIc("COUNTRY", "UNKNOWN_COUNTRY"))
                .thenReturn(Optional.empty());


        String expectedDescription = "Country = UNKNOWN_COUNTRY not supported!";
        ValidationErrorDTO expectedError = new ValidationErrorDTO("ERROR_CODE_11", expectedDescription);

        when(errorFactory.buildError(eq("ERROR_CODE_11"), anyList())).thenReturn(expectedError);

        Optional<ValidationErrorDTO> result = validation.validate(request);

        assertTrue(result.isPresent());
        assertEquals(expectedError, result.get());

        verify(classifierValueRepository).findByClassifierTitleAndIc("COUNTRY", "UNKNOWN_COUNTRY");
        verify(errorFactory).buildError(eq("ERROR_CODE_11"), anyList());
    }

}
