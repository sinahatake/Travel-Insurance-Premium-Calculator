package org.javaguru.travel.insurance.dto.v2;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreResult;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.dto.RiskPremium;
import org.javaguru.travel.insurance.dto.ValidationError;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DtoV2Converter {

    public TravelCalculatePremiumCoreCommand buildCoreCommand(TravelCalculatePremiumRequestV2 request) {
        AgreementDTO agreement = buildAgreement(request);
        return new TravelCalculatePremiumCoreCommand(agreement);
    }

    public TravelCalculatePremiumResponseV2 buildResponse(TravelCalculatePremiumCoreResult coreResult) {
        return coreResult.hasErrors()
                ? buildResponseWithErrors(coreResult.getErrors())
                : buildSuccessfulResponse(coreResult);
    }

    private AgreementDTO buildAgreement(TravelCalculatePremiumRequestV2 request) {
        AgreementDTO agreement = new AgreementDTO();
        agreement.setAgreementDateTo(request.getAgreementDateTo());
        agreement.setAgreementDateFrom(request.getAgreementDateFrom());
        agreement.setCountry(request.getCountry());
        agreement.setSelectedRisks(request.getSelectedRisks());
        agreement.setPersons(buildPerson(request));
        return agreement;
    }

    private List<PersonDTO> buildPerson(TravelCalculatePremiumRequestV2 request) {
        if (request.getPersons() == null) {
        return List.of();
    }
        return request.getPersons().stream()
                .map(reqPerson -> {
                    PersonDTO person = new PersonDTO();
                    person.setPersonCode(reqPerson.getPersonCode());
                    person.setPersonBirthDate(reqPerson.getPersonBirthDate());
                    person.setPersonFirstName(reqPerson.getPersonFirstName());
                    person.setPersonLastName(reqPerson.getPersonLastName());
                    person.setMedicalRiskLimitLevel(reqPerson.getMedicalRiskLimitLevel());
                    return person;
                })
                .toList();
    }


    private TravelCalculatePremiumResponseV2 buildResponseWithErrors(List<ValidationErrorDTO> errors) {
        return new TravelCalculatePremiumResponseV2(transformValidationErrorsToV1(errors));
    }

    private List<ValidationError> transformValidationErrorsToV1(List<ValidationErrorDTO> errors) {
        return errors.stream()
                .map(error -> new ValidationError(error.getErrorCode(), error.getDescription()))
                .toList();

    }

    private TravelCalculatePremiumResponseV2 buildSuccessfulResponse(TravelCalculatePremiumCoreResult coreResult) {
        TravelCalculatePremiumResponseV2 response = buildAgreementPart(coreResult);
        response.setPersons(mapPersons(coreResult));
        return response;
    }

    private TravelCalculatePremiumResponseV2 buildAgreementPart(TravelCalculatePremiumCoreResult coreResult) {
        TravelCalculatePremiumResponseV2 response = new TravelCalculatePremiumResponseV2();
        var agreement = coreResult.getAgreement();
        response.setUuid(agreement.getUuid());
        response.setAgreementPremium(agreement.getAgreementPremium());
        response.setAgreementDateTo(agreement.getAgreementDateTo());
        response.setAgreementDateFrom(agreement.getAgreementDateFrom());
        response.setCountry(agreement.getCountry());
        return response;
    }

    private List<PersonResponseDTO> mapPersons(TravelCalculatePremiumCoreResult coreResult) {
        return coreResult.getAgreement().getPersons().stream()
                .map(reqPerson -> {
                    PersonResponseDTO person = new PersonResponseDTO();
                    person.setPersonCode(reqPerson.getPersonCode());
                    person.setPersonBirthDate(reqPerson.getPersonBirthDate());
                    person.setPersonFirstName(reqPerson.getPersonFirstName());
                    person.setPersonLastName(reqPerson.getPersonLastName());
                    person.setMedicalRiskLimitLevel(reqPerson.getMedicalRiskLimitLevel());
                    person.setPersonPremium(mapPersonPremium(reqPerson.getRisks()));
                    person.setPersonRisks(mapRisks(reqPerson.getRisks()));
                    return person;
                })
                .toList();
    }

    private BigDecimal mapPersonPremium(List<RiskDTO> risks) {
        return risks.stream()
                .map(RiskDTO::getPremium)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<RiskPremium> mapRisks(List<RiskDTO> risks) {
        return risks.stream()
                .map(risk -> new RiskPremium(risk.getRiskIc(), risk.getPremium()))
                .toList();
    }


}
