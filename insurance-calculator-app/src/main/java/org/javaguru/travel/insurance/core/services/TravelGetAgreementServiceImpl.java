package org.javaguru.travel.insurance.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreResult;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.validations.TravelAgreementUuidValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TravelGetAgreementServiceImpl implements TravelGetAgreementService {
    private final TravelAgreementUuidValidator travelAgreementUuidValidator;
    private final AgreementDTOLoader agreementDTOLoader;

    @Override
    public TravelGetAgreementCoreResult getAgreement(TravelGetAgreementCoreCommand command) {

        List<ValidationErrorDTO> errors = travelAgreementUuidValidator.validate(command.getUuid());
        if (errors.isEmpty()) {
            return buildResponse(command.getUuid());
        } else {
            return buildResponse(errors);
        }
    }

    private TravelGetAgreementCoreResult buildResponse(List<ValidationErrorDTO> errors) {
        return new TravelGetAgreementCoreResult(errors);
    }

    private TravelGetAgreementCoreResult buildResponse(String uuid) {
        return agreementDTOLoader.load(uuid);
    }
}
