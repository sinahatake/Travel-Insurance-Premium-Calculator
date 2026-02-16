package org.javaguru.travel.insurance.dto.internal;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreResult;
import org.javaguru.travel.insurance.core.api.dto.RiskDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.dto.RiskPremium;
import org.javaguru.travel.insurance.dto.ValidationError;
import org.javaguru.travel.insurance.dto.v2.PersonResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DtoInternalConverter {

    public TravelGetAgreementCoreCommand buildCoreCommand(String uuid) {
        return new TravelGetAgreementCoreCommand(uuid);
    }

    public TravelGetAgreementResponse buildResponse(TravelGetAgreementCoreResult coreResult) {
        return coreResult.hasErrors()
                ? buildResponseWithErrors(coreResult.getErrors())
                : buildSuccessfulResponse(coreResult);
    }

    private TravelGetAgreementResponse buildResponseWithErrors(List<ValidationErrorDTO> errors) {
        return new TravelGetAgreementResponse(transformValidationErrorsToV1(errors));
    }

    private List<ValidationError> transformValidationErrorsToV1(List<ValidationErrorDTO> errors) {
        return errors.stream()
                .map(error -> new ValidationError(error.getErrorCode(), error.getDescription()))
                .toList();

    }

    private TravelGetAgreementResponse buildSuccessfulResponse(TravelGetAgreementCoreResult coreResult) {
        TravelGetAgreementResponse response = buildAgreementPart(coreResult);
        response.setPersons(mapPersons(coreResult));
        return response;
    }

    private TravelGetAgreementResponse buildAgreementPart(TravelGetAgreementCoreResult coreResult) {
        TravelGetAgreementResponse response = new TravelGetAgreementResponse();
        var agreement = coreResult.getAgreement();
        response.setUuid(agreement.getUuid());
        response.setAgreementPremium(agreement.getAgreementPremium());
        response.setAgreementDateTo(agreement.getAgreementDateTo());
        response.setAgreementDateFrom(agreement.getAgreementDateFrom());
        response.setCountry(agreement.getCountry());
        return response;
    }

    private List<PersonResponseDTO> mapPersons(TravelGetAgreementCoreResult coreResult) {
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
