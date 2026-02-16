package org.javaguru.travel.insurance.core.services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaguru.travel.insurance.core.api.command.TravelExportAgreementToXmlCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelExportAgreementToXmlCoreResult;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreResult;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.domain.entities.AgreementXmlExportEntity;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementXmlExportEntityRepository;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
public class TravelExportAgreementToXmlServiceImpl implements TravelExportAgreementToXmlService {
    @Value("${agreement.xml.exporter.path}")
    private String path;

    private final TravelGetAgreementService travelGetAgreementService;
    private final AgreementXmlExportEntityRepository agreementXmlExportEntityRepository;
    private final ValidationErrorFactory errorFactory;

    @Override
    public TravelExportAgreementToXmlCoreResult export(TravelExportAgreementToXmlCoreCommand command) {
        try {
            String uuid = command.getUuid();
            exportAgreementToXml(getAgreementDTO(uuid));
            saveAgreementXmlExportEntity(uuid);
        } catch (Exception e) {
            log.info("AgreementXmlExporterJob failed for agreement uuid = {}", command.getUuid(), e);
            return new TravelExportAgreementToXmlCoreResult(List.of(
                    errorFactory.buildError("ERROR_CODE_19")));
        }
        return new TravelExportAgreementToXmlCoreResult();
    }

    private void exportAgreementToXml(AgreementDTO agreement) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AgreementDTO.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(agreement, new File(path + "/agreement-" + agreement.getUuid() + ".xml"));
    }

    private AgreementDTO getAgreementDTO(String uuid) {
        TravelGetAgreementCoreCommand cmd = new TravelGetAgreementCoreCommand(uuid);
        TravelGetAgreementCoreResult result = travelGetAgreementService.getAgreement(cmd);
        return result.getAgreement();
    }


    private void saveAgreementXmlExportEntity(String uuid) {
        AgreementXmlExportEntity agreementXmlExportEntity = new AgreementXmlExportEntity();
        agreementXmlExportEntity.setAgreementUuid(uuid);
        agreementXmlExportEntity.setAlreadyExported(true);
        agreementXmlExportEntityRepository.save(agreementXmlExportEntity);
    }


}
