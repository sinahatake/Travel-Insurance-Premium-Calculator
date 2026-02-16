package org.javaguru.travel.insurance.core.validations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TravelAgreementFieldValidator {

    private final List<TravelAgreementFieldValidation> travelAgreementFieldValidations;

    public List<ValidationErrorDTO> validate(AgreementDTO agreementDTO) {
        List<ValidationErrorDTO> singleErrors = collectAgreementSingleErrors(agreementDTO);
        List<ValidationErrorDTO> listErrors = collectAgreementListErrors(agreementDTO);
        return concatenateLists(singleErrors, listErrors);
    }

    private List<ValidationErrorDTO> collectAgreementSingleErrors(AgreementDTO agreementDTO) {
        return travelAgreementFieldValidations.stream()
                .map(validation -> validation.validate(agreementDTO))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private List<ValidationErrorDTO> collectAgreementListErrors(AgreementDTO agreementDTO) {
        return travelAgreementFieldValidations.stream()
                .map(validation -> validation.validateList(agreementDTO))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<ValidationErrorDTO> concatenateLists(List<ValidationErrorDTO> singleErrors,
                                                      List<ValidationErrorDTO> listErrors) {
        return Stream.concat(singleErrors.stream(), listErrors.stream())
                .toList();
    }

}
