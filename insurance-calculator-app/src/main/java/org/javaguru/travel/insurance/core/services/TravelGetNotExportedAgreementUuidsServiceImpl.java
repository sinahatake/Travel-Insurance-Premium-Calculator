package org.javaguru.travel.insurance.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelGetNotExportedAgreementUuidsCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetNotExportedAgreementUuidsCoreResult;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementEntityRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TravelGetNotExportedAgreementUuidsServiceImpl implements TravelGetNotExportedAgreementUuidsService {
    private final AgreementEntityRepository agreementEntityRepository;

    @Override
    public TravelGetNotExportedAgreementUuidsCoreResult getNotExportedAgreement(TravelGetNotExportedAgreementUuidsCoreCommand command) {
        List<String> uuids = agreementEntityRepository.getNotExportedAgreementUuids();
        return new TravelGetNotExportedAgreementUuidsCoreResult(null, uuids);
    }
}
