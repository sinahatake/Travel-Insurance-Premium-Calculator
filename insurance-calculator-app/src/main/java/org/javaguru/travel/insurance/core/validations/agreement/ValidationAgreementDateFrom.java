package org.javaguru.travel.insurance.core.validations.agreement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;

import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
class ValidationAgreementDateFrom extends TravelAgreementFieldValidationImpl {
    private final ValidationErrorFactory errorFactory;

    @Override
    public Optional<ValidationErrorDTO> validate(AgreementDTO agreementDTO) {
        return (agreementDTO.getAgreementDateFrom() == null)
                ? Optional.of(errorFactory.buildError("ERROR_CODE_2"))
                : Optional.empty();
    }
}



