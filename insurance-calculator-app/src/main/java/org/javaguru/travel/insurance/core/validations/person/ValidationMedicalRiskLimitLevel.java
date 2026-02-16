package org.javaguru.travel.insurance.core.validations.person;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.repositories.ClassifierValueRepository;
import org.javaguru.travel.insurance.core.util.Placeholder;
import org.javaguru.travel.insurance.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class ValidationMedicalRiskLimitLevel extends TravelPersonFieldValidationImpl {
    private final ValidationErrorFactory errorFactory;
    private final ClassifierValueRepository classifierValueRepository;

    @Override
    public Optional<ValidationErrorDTO> validate(PersonDTO person) {
        String medicalRiskLimitLevel = person.getMedicalRiskLimitLevel();

        return (!isMedicalRiskLimitLevelNull(person) &&
                !existInDatabase(medicalRiskLimitLevel))
                ? Optional.of(buildValidationErrorDTO(medicalRiskLimitLevel))
                : Optional.empty();

    }

    private boolean isMedicalRiskLimitLevelNull(PersonDTO person) {
        return person.getMedicalRiskLimitLevel() == null || person.getMedicalRiskLimitLevel().isBlank();
    }


    private boolean existInDatabase(String medicalRiskLimitLevel) {
        return classifierValueRepository.findByClassifierTitleAndIc("MEDICAL_RISK_LIMIT_LEVEL", medicalRiskLimitLevel).isPresent();
    }

    private ValidationErrorDTO buildValidationErrorDTO(String level) {
        Placeholder placeholder = new Placeholder("NOT_EXISTING_LEVEL_TYPE", level);
        return errorFactory.buildError("ERROR_CODE_15", List.of(placeholder));
    }
}
