package org.javaguru.travel.insurance.core.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreResult;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
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

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class AgreementDTOLoader {
    private final AgreementEntityRepository repository;
    private final SelectedRiskEntityRepository selectedRiskRepository;
    private final AgreementPersonEntityRepository agreementPersonRepository;
    private final AgreementPersonRiskEntityRepository agreementPersonRiskEntityRepository;

    TravelGetAgreementCoreResult load(String uuid) {
        AgreementEntity agreementEntity = repository.findByUuid(uuid).get();
        TravelGetAgreementCoreResult result = new TravelGetAgreementCoreResult();
        result.setAgreement(agreementLoad(agreementEntity));

        return result;
    }

    AgreementDTO agreementLoad(AgreementEntity agreementEntity) {
        AgreementDTO agreementDTO = new AgreementDTO();

        agreementDTO.setAgreementDateTo(agreementEntity.getAgreementDateTo());
        agreementDTO.setAgreementDateFrom(agreementEntity.getAgreementDateFrom());
        agreementDTO.setUuid(agreementEntity.getUuid());
        agreementDTO.setCountry(agreementEntity.getCountry());
        agreementDTO.setAgreementPremium(agreementEntity.getAgreementPremium());
        agreementDTO.setPersons(personLoad(agreementEntity));
        agreementDTO.setSelectedRisks(selectedRiskLoad(agreementEntity));
        return agreementDTO;
    }

    List<String> selectedRiskLoad(AgreementEntity agreementEntity) {
        List<SelectedRiskEntity> selectedRiskEntity = selectedRiskRepository.findByAgreement(agreementEntity);

        return selectedRiskEntity
                .stream()
                .map(SelectedRiskEntity::getRiskIc)
                .collect(Collectors.toList());
    }


    List<PersonDTO> personLoad(AgreementEntity agreementEntity) {
        List<AgreementPersonEntity> agreementPersonEntities = agreementPersonRepository.findByAgreement(agreementEntity);

        return agreementPersonEntities
                .stream()
                .map(this::convertToPersonDTO)
                .collect(Collectors.toList());
    }

    private RiskDTO convertToRiskDTO(AgreementPersonRiskEntity riskEntity) {
        return RiskDTO.builder()
                .riskIc(riskEntity.getRiskIc())
                .premium(riskEntity.getPremium())
                .build();
    }

    private PersonDTO convertToPersonDTO(AgreementPersonEntity agreementPersonEntity) {
        PersonEntity personEntity = agreementPersonEntity.getPerson();

        List<RiskDTO> risks = agreementPersonRiskEntityRepository
                .findByAgreementPerson(agreementPersonEntity)
                .stream()
                .map(this::convertToRiskDTO)
                .collect(Collectors.toList());

        return PersonDTO.builder()
                .personCode(personEntity.getPersonCode())
                .personFirstName(personEntity.getFirstName())
                .personLastName(personEntity.getLastName())
                .personBirthDate(personEntity.getBirthDate())
                .medicalRiskLimitLevel(agreementPersonEntity.getMedicalRiskLimitLevel())
                .risks(risks)
                .build();
    }


}
