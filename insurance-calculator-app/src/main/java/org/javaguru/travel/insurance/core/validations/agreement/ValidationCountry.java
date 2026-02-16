package org.javaguru.travel.insurance.core.validations.agreement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.repositories.ClassifierValueRepository;

import org.javaguru.travel.insurance.core.util.Placeholder;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class ValidationCountry extends TravelAgreementFieldValidationImpl {

    private final ValidationErrorFactory errorFactory;
    private final ClassifierValueRepository classifierValueRepository;

    @Override
    public Optional<ValidationErrorDTO> validate(AgreementDTO agreementDTO) {
        String country = agreementDTO.getCountry();
        return (travelMedicalSelected(agreementDTO) &&
                !emptyCountry(agreementDTO) &&
                !existInDatabase(country))
                ? Optional.of(buildValidationErrorDTO(country))
                : Optional.empty();

    }

    private boolean emptyCountry(AgreementDTO agreementDTO) {
        return (agreementDTO.getCountry() == null || agreementDTO.getCountry().isEmpty());
    }

    private boolean travelMedicalSelected(AgreementDTO agreementDTO) {
        return agreementDTO.getSelectedRisks() != null && agreementDTO.getSelectedRisks().contains("TRAVEL_MEDICAL");
    }

    private ValidationErrorDTO buildValidationErrorDTO(String country) {
        Placeholder placeholder = new Placeholder("NOT_EXISTING_COUNTRY_TYPE", country);
        return errorFactory.buildError("ERROR_CODE_11", List.of(placeholder));
    }

    private boolean existInDatabase(String country) {
        return classifierValueRepository.findByClassifierTitleAndIc("COUNTRY", country).isPresent();
    }
}
