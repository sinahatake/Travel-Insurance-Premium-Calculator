package org.javaguru.travel.insurance.core.underwriting.integration.medical;

import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.blacklist.BlackListedPersonService;
import org.javaguru.travel.insurance.core.messagebroker.proposal.ProposalGeneratorQueueSender;
import org.javaguru.travel.insurance.core.underwriting.TravelPremiumCalculationResult;
import org.javaguru.travel.insurance.core.underwriting.TravelPremiumUnderwriting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"age.coefficient.enabled=false"})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AgeCoefficientSwitchDisabledIntegrationTest {

    @MockitoBean
    private BlackListedPersonService blackListedPersonService;


    @MockitoBean
    private ProposalGeneratorQueueSender sender;

    @Autowired
    private TravelPremiumUnderwriting premiumUnderwriting;

    @Test
    public void shouldBeEnabledAgeCoefficient() {
        PersonDTO person = PersonDTO.builder()
                .personFirstName("Vasja")
                .personLastName("Pupkin")
                .personBirthDate(LocalDate.of(2000, 1, 1))
                .medicalRiskLimitLevel("LEVEL_20000")
                .build();

        AgreementDTO agreement = AgreementDTO.builder()
                .agreementDateFrom(LocalDate.of(2030, 1, 1))
                .agreementDateTo(LocalDate.of(2030, 5, 1))
                .country("SPAIN")
                .selectedRisks(List.of("TRAVEL_MEDICAL"))
                .persons(List.of(person))
                .build();

        TravelPremiumCalculationResult result = premiumUnderwriting.calculatePremium(agreement, person);

        assertEquals(new BigDecimal("450.00"), result.totalPremium());
    }
}
