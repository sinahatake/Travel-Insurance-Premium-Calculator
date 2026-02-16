package org.javaguru.travel.insurance.core.validations.integration;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.blacklist.BlackListedPersonService;
import org.javaguru.travel.insurance.core.messagebroker.proposal.ProposalGeneratorQueueSender;
import org.javaguru.travel.insurance.core.validations.TravelAgreementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ValidationPersonLastNameIntegrationTest {
    @MockitoBean
    private BlackListedPersonService blackListedPersonService;

    @MockitoBean
    private ProposalGeneratorQueueSender sender;

    @Autowired
    private TravelAgreementValidator validator;

    @Test
    public void shouldReturnErrorWhenPersonLastNameIsNull() {
        PersonDTO person = PersonDTO.builder()
                .personCode("010882-11034")
                .personFirstName("Vasja")
                .personLastName(null)
                .personBirthDate(LocalDate.of(2000, 1, 1))
                .medicalRiskLimitLevel("LEVEL_10000")
                .build();

        AgreementDTO agreement = AgreementDTO.builder()
                .agreementDateFrom(LocalDate.of(2029, 1, 1))
                .agreementDateTo(LocalDate.of(2030, 1, 1))
                .country("SPAIN")
                .selectedRisks(List.of("TRAVEL_MEDICAL"))
                .persons(List.of(person))
                .build();

        List<ValidationErrorDTO> errors = validator.validate(agreement);

        assertEquals(1, errors.size());
        assertEquals("ERROR_CODE_8", errors.getFirst().getErrorCode());
        assertEquals("Person Last Name Must not be empty!", errors.getFirst().getDescription());
    }

    @Test
    public void shouldReturnErrorWhenPersonLastNameIsEmpty() {
        PersonDTO person = PersonDTO.builder()
                .personCode("010882-11034")
                .personFirstName("Vasja")
                .personLastName("")
                .personBirthDate(LocalDate.of(2000, 1, 1))
                .medicalRiskLimitLevel("LEVEL_10000")
                .build();

        AgreementDTO agreement = AgreementDTO.builder()
                .agreementDateFrom(LocalDate.of(2029, 1, 1))
                .agreementDateTo(LocalDate.of(2030, 1, 1))
                .country("SPAIN")
                .selectedRisks(List.of("TRAVEL_MEDICAL"))
                .persons(List.of(person))
                .build();

        List<ValidationErrorDTO> errors = validator.validate(agreement);

        assertEquals(1, errors.size());
        assertEquals("ERROR_CODE_8", errors.getFirst().getErrorCode());
        assertEquals("Person Last Name Must not be empty!", errors.getFirst().getDescription());
    }

    @Test
    public void shouldNotReturnErrorWhenPersonLastNameIsPresent() {
        PersonDTO person = PersonDTO.builder()
                .personCode("010882-11034")
                .personFirstName("Vasja")
                .personLastName("Pupkin")
                .personBirthDate(LocalDate.of(2000, 1, 1))
                .medicalRiskLimitLevel("LEVEL_10000")
                .build();

        AgreementDTO agreement = AgreementDTO.builder()
                .agreementDateFrom(LocalDate.of(2029, 1, 1))
                .agreementDateTo(LocalDate.of(2030, 1, 1))
                .country("SPAIN")
                .selectedRisks(List.of("TRAVEL_MEDICAL"))
                .persons(List.of(person))
                .build();

        List<ValidationErrorDTO> errors = validator.validate(agreement);

        assertEquals(0, errors.size());
    }
}
