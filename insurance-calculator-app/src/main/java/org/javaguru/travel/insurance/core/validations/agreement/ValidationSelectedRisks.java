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
public class ValidationSelectedRisks extends TravelAgreementFieldValidationImpl {
    private final ValidationErrorFactory errorFactory;
    private final ClassifierValueRepository classifierValueRepository;

    @Override
    public List<ValidationErrorDTO> validateList(AgreementDTO agreementDTO) {
        return agreementDTO.getSelectedRisks() != null
                ? validateSelectedRisks(agreementDTO)
                : List.of();
    }

    private List<ValidationErrorDTO> validateSelectedRisks(AgreementDTO agreementDTO) {
        return agreementDTO.getSelectedRisks().stream()
                .map(this::validateRiskIc)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<ValidationErrorDTO> validateRiskIc(String riskIc) {
        return !existInDatabase(riskIc)
                ? Optional.of(buildValidationErrorDTO(riskIc))
                : Optional.empty();
    }

    private ValidationErrorDTO buildValidationErrorDTO(String riskIc) {
        Placeholder placeholder = new Placeholder("NOT_EXISTING_RISK_TYPE", riskIc);
        return errorFactory.buildError("ERROR_CODE_9", List.of(placeholder));
    }

    private boolean existInDatabase(String riskIc) {
        return classifierValueRepository.findByClassifierTitleAndIc("RISK_TYPE", riskIc).isPresent();
    }
}
