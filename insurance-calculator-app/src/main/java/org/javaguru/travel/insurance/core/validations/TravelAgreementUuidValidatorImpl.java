package org.javaguru.travel.insurance.core.validations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementEntityRepository;
import org.javaguru.travel.insurance.core.util.Placeholder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TravelAgreementUuidValidatorImpl implements TravelAgreementUuidValidator {
    private final ValidationErrorFactory errorFactory;
    private final AgreementEntityRepository agreementEntityRepository;

    @Override
    public List<ValidationErrorDTO> validate(String agreementUuid) {
        List<ValidationErrorDTO> errors = new ArrayList<>();
        if (isAgreementUuidIsNull(agreementUuid)) {
            errors.add(errorFactory.buildError("ERROR_CODE_17"));
        } else if (!existInDatabase(agreementUuid)) {
            errors.add(buildValidationErrorDTO(agreementUuid));
        }
        return errors;
    }

    private boolean isAgreementUuidIsNull(String agreementUuid) {
        return agreementUuid == null || agreementUuid.isBlank();
    }


    private boolean existInDatabase(String agreementUuid) {
        return agreementEntityRepository.findByUuid(agreementUuid).isPresent();
    }

    private ValidationErrorDTO buildValidationErrorDTO(String agreementUuid) {
        Placeholder placeholder = new Placeholder("NOT_EXISTING_UUID", agreementUuid);
        return errorFactory.buildError("ERROR_CODE_18", List.of(placeholder));
    }
}

