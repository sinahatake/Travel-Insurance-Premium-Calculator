package org.javaguru.travel.insurance.dto.v1;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreResult;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.dto.RiskPremium;
import org.javaguru.travel.insurance.dto.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DtoV1Converter {

    public TravelCalculatePremiumCoreCommand buildCoreCommand(TravelCalculatePremiumRequestV1 request) {
        AgreementDTO agreement = buildAgreement(request);
        return new TravelCalculatePremiumCoreCommand(agreement);
    }

    public TravelCalculatePremiumResponseV1 buildResponse(TravelCalculatePremiumCoreResult coreResult) {
        return coreResult.hasErrors()
                ? buildResponseWithErrors(coreResult.getErrors())
                : buildSuccessfulResponse(coreResult);
    }

    private AgreementDTO buildAgreement(TravelCalculatePremiumRequestV1 request) {
        AgreementDTO agreement = new AgreementDTO();
        agreement.setAgreementDateTo(request.getAgreementDateTo());
        agreement.setAgreementDateFrom(request.getAgreementDateFrom());
        agreement.setCountry(request.getCountry());
        agreement.setSelectedRisks(request.getSelectedRisks());

        PersonDTO person = buildPerson(request);
        agreement.setPersons(List.of(person));
        return agreement;
    }

    private PersonDTO buildPerson(TravelCalculatePremiumRequestV1 request) {
        PersonDTO person = new PersonDTO();
        person.setPersonCode(request.getPersonCode());
        person.setPersonBirthDate(request.getPersonBirthDate());
        person.setPersonFirstName(request.getPersonFirstName());
        person.setPersonLastName(request.getPersonLastName());
        person.setMedicalRiskLimitLevel(request.getMedicalRiskLimitLevel());
        return person;
    }

    private TravelCalculatePremiumResponseV1 buildResponseWithErrors(List<ValidationErrorDTO> errors) {
        return new TravelCalculatePremiumResponseV1(transformValidationErrorsToV1(errors));
    }

    private List<ValidationError> transformValidationErrorsToV1(List<ValidationErrorDTO> errors) {
        return errors.stream()
                .map(error -> new ValidationError(error.getErrorCode(), error.getDescription()))
                .toList();

    }

    private TravelCalculatePremiumResponseV1 buildSuccessfulResponse(TravelCalculatePremiumCoreResult coreResult) {
        TravelCalculatePremiumResponseV1 response = new TravelCalculatePremiumResponseV1();
        response.setUuid(coreResult.getAgreement().getUuid());
        response.setAgreementPremium(coreResult.getAgreement().getAgreementPremium());
        response.setAgreementDateTo(coreResult.getAgreement().getAgreementDateTo());
        response.setAgreementDateFrom(coreResult.getAgreement().getAgreementDateFrom());
        response.setCountry(coreResult.getAgreement().getCountry());

        response.setPersonCode(coreResult.getAgreement().getPersons().getFirst().getPersonCode());
        response.setPersonFirstName(coreResult.getAgreement().getPersons().getFirst().getPersonFirstName());
        response.setPersonLastName(coreResult.getAgreement().getPersons().getLast().getPersonLastName());
        response.setPersonBirthDate(coreResult.getAgreement().getPersons().getFirst().getPersonBirthDate());
        response.setMedicalRiskLimitLevel(coreResult.getAgreement().getPersons().getFirst().getMedicalRiskLimitLevel());

        List<RiskPremium> riskPremiums = coreResult.getAgreement().getPersons().getFirst().getRisks().stream()
                .map(risk -> new RiskPremium(risk.getRiskIc(), risk.getPremium()))
                .toList();
        response.setSelectedRisks(riskPremiums);

        return response;

    }


}
