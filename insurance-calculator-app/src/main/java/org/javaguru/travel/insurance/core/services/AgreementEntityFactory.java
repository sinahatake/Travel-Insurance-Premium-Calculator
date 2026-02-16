package org.javaguru.travel.insurance.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.domain.entities.AgreementPersonEntity;
import org.javaguru.travel.insurance.core.domain.entities.AgreementPersonRiskEntity;
import org.javaguru.travel.insurance.core.domain.entities.PersonEntity;
import org.javaguru.travel.insurance.core.domain.entities.SelectedRiskEntity;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementEntityRepository;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementPersonEntityRepository;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementPersonRiskEntityRepository;
import org.javaguru.travel.insurance.core.repositories.entities.SelectedRiskEntityRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AgreementEntityFactory {
    private final AgreementEntityRepository agreementEntityRepository;
    private final PersonEntityFactory personEntityFactory;
    private final SelectedRiskEntityRepository selectedRiskEntityRepository;
    private final AgreementPersonEntityRepository agreementPersonEntityRepository;
    private final AgreementPersonRiskEntityRepository agreementPersonRiskEntityRepository;

    public AgreementEntity createAgreementEntity(AgreementDTO agreementDTO) {
        AgreementEntity agreementEntity = saveAgreementEntity(agreementDTO);
        saveAllAgreementPersons(agreementDTO, agreementEntity);
        saveRisks(agreementDTO, agreementEntity);
        return agreementEntityRepository.save(agreementEntity);
    }

    private AgreementEntity saveAgreementEntity(AgreementDTO agreementDTO) {
        AgreementEntity agreementEntity = new AgreementEntity();
        agreementEntity.setUuid(UUID.randomUUID().toString());
        agreementEntity.setAgreementDateFrom(agreementDTO.getAgreementDateFrom());
        agreementEntity.setAgreementDateTo(agreementDTO.getAgreementDateTo());
        agreementEntity.setCountry(agreementDTO.getCountry());
        agreementEntity.setAgreementPremium(agreementDTO.getAgreementPremium());

        return agreementEntityRepository.save(agreementEntity);
    }


    private void saveRisks(AgreementDTO agreementDTO, AgreementEntity agreementEntity) {
        for (String riskIc : agreementDTO.getSelectedRisks()) {
            SelectedRiskEntity riskEntity = new SelectedRiskEntity();
            riskEntity.setAgreement(agreementEntity);
            riskEntity.setRiskIc(riskIc);
            selectedRiskEntityRepository.save(riskEntity);
        }

    }

    private void saveAgreementPersonRisks(PersonDTO personDTO, AgreementPersonEntity agreementPersonEntity) {
        personDTO.getRisks().forEach(riskDTO -> {
            AgreementPersonRiskEntity agreementPersonRiskEntity = new AgreementPersonRiskEntity();
            agreementPersonRiskEntity.setAgreementPerson(agreementPersonEntity);
            agreementPersonRiskEntity.setPremium(riskDTO.getPremium());
            agreementPersonRiskEntity.setRiskIc(riskDTO.getRiskIc());
            agreementPersonRiskEntityRepository.save(agreementPersonRiskEntity);
        });
    }

    private void saveAllAgreementPersons(AgreementDTO agreementDTO, AgreementEntity agreementEntity) {
        agreementDTO.getPersons().forEach(personDTO -> {
            AgreementPersonEntity agreementPersonEntity = new AgreementPersonEntity();
            PersonEntity personEntity = personEntityFactory.createPersonEntity(personDTO);
            agreementPersonEntity.setPerson(personEntity);
            agreementPersonEntity.setAgreement(agreementEntity);
            agreementPersonEntity.setMedicalRiskLimitLevel(personDTO.getMedicalRiskLimitLevel());
            agreementPersonEntityRepository.save(agreementPersonEntity);

            saveAgreementPersonRisks(personDTO, agreementPersonEntity);
        });


    }

}
