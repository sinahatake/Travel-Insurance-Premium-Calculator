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
public class ValidationCountryNotEmpty extends TravelAgreementFieldValidationImpl {
    private final ValidationErrorFactory errorFactory;

    @Override
    public Optional<ValidationErrorDTO> validate(AgreementDTO agreementDTO) {
        return (emptyCountry(agreementDTO))
            ? Optional.of(errorFactory.buildError("ERROR_CODE_10"))
            : Optional.empty();
    }

    private boolean emptyCountry(AgreementDTO agreementDTO) {
         return (agreementDTO.getCountry() == null || agreementDTO.getCountry().isEmpty());
    }

}
