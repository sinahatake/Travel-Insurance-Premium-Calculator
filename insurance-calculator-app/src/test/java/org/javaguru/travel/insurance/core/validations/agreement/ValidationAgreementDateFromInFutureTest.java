package org.javaguru.travel.insurance.core.validations.agreement;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationAgreementDateFromInFutureTest {
    @Mock
    private ValidationErrorFactory errorFactory;

    @InjectMocks
    private ValidationAgreementDateFromInFuture validation;

    @Test
    public void shouldReturnErrorWhenDateFromIsInPast() {
        AgreementDTO request = mock(AgreementDTO.class);
        when(request.getAgreementDateFrom()).thenReturn(LocalDate.now().minusDays(10));

        ValidationErrorDTO ValidationErrorDTO = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_1")).thenReturn(ValidationErrorDTO);

        Optional<ValidationErrorDTO> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertSame(errorOpt.get(), ValidationErrorDTO);
    }

    @Test
    public void shouldNotReturnErrorWhenDateFromIsInFuture() {
        AgreementDTO request = mock(AgreementDTO.class);
        when(request.getAgreementDateFrom()).thenReturn(LocalDate.now().plusDays(10));

        Optional<ValidationErrorDTO> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isEmpty());
        verifyNoInteractions(errorFactory);
    }

}