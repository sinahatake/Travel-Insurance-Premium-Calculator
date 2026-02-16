package org.javaguru.travel.insurance.core.validations.agreement;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationAgreementDateToTest {
    @Mock private ValidationErrorFactory errorFactory;

    @InjectMocks
    private ValidationAgreementDateTo validation;

    @Test
    public void shouldReturnErrorWhenAgreementDateToIsNull() {
        AgreementDTO request = mock(AgreementDTO.class);
        when(request.getAgreementDateTo()).thenReturn(null);

        ValidationErrorDTO ValidationErrorDTO = mock(ValidationErrorDTO.class);
        when(errorFactory.buildError("ERROR_CODE_4")).thenReturn(ValidationErrorDTO);

        Optional<ValidationErrorDTO> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertSame(errorOpt.get(), ValidationErrorDTO);
    }

    @Test
    public void shouldNotReturnErrorWhenAgreementDateToIsNotNull() {
        AgreementDTO request = mock(AgreementDTO.class);
        when(request.getAgreementDateTo()).thenReturn(now());

        Optional<ValidationErrorDTO> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isEmpty());
        verifyNoInteractions(errorFactory);
    }
}