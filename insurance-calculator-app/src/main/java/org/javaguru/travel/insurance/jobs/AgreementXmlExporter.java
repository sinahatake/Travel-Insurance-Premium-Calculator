package org.javaguru.travel.insurance.jobs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaguru.travel.insurance.core.api.command.TravelExportAgreementToXmlCoreCommand;
import org.javaguru.travel.insurance.core.services.TravelExportAgreementToXmlService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AgreementXmlExporter {

    private final TravelExportAgreementToXmlService agreementToXmlService;
    
    public void exportAgreement(String agreementUuid) {
        log.info("AgreementXmlExporterJob started for uuid = {}", agreementUuid);
        agreementToXmlService.export(new TravelExportAgreementToXmlCoreCommand(agreementUuid));
        log.info("AgreementXmlExporterJob finished for uuid = {}", agreementUuid);
    }

}
