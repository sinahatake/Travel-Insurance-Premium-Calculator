package org.javaguru.travel.insurance.core.validations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TravelAgreementValidatorImpl implements TravelAgreementValidator {

    private final TravelAgreementFieldValidator travelAgreementFieldValidator;
    private final TravelPersonFieldValidator travelPersonFieldValidator;

    @Override
    public List<ValidationErrorDTO> validate(AgreementDTO agreementDTO) {
        List<ValidationErrorDTO> AgreementErrors = travelAgreementFieldValidator.validate(agreementDTO);
        List<ValidationErrorDTO> PersonsErrors = travelPersonFieldValidator.validate(agreementDTO);
        return concatenateLists(AgreementErrors, PersonsErrors);
    }

    private List<ValidationErrorDTO> concatenateLists(List<ValidationErrorDTO> singleErrors,
                                                      List<ValidationErrorDTO> listErrors) {
        return Stream.concat(singleErrors.stream(), listErrors.stream())
                .toList();
    }


}
