package org.javaguru.travel.insurance.core.services;

import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreResult;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.api.dto.ValidationErrorDTO;
import org.javaguru.travel.insurance.core.domain.entities.AgreementEntity;
import org.javaguru.travel.insurance.core.messagebroker.proposal.ProposalGeneratorQueueSender;
import org.javaguru.travel.insurance.core.validations.TravelAgreementValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelCalculatePremiumServiceImplTest {

    @Mock
    private TravelAgreementValidator validator;

    @Mock
    private AgreementPersonsPremiumCalculator personsPremiumCalculator;

    @Mock
    private AgreementTotalPremiumCalculator totalPremiumCalculator;

    @Mock
    private AgreementEntityFactory entityFactory;

    @Mock
    private ProposalGeneratorQueueSender sender;

    @InjectMocks
    private TravelCalculatePremiumServiceImpl service;

    @Test
    void shouldReturnValidationErrors() {
        TravelCalculatePremiumCoreCommand command = new TravelCalculatePremiumCoreCommand();
        AgreementDTO agreement = new AgreementDTO();
        command.setAgreement(agreement);

        List<ValidationErrorDTO> errors = List.of(
                new ValidationErrorDTO("field", "must not be null")
        );

        when(validator.validate(agreement)).thenReturn(errors);

        TravelCalculatePremiumCoreResult result = service.calculatePremium(command);

        assertEquals(errors, result.getErrors());
        assertNull(result.getAgreement());
        verifyNoInteractions(personsPremiumCalculator, totalPremiumCalculator);
    }

    @Test
    void shouldCalculatePremiumSuccessfully() {
        PersonDTO person = new PersonDTO();

        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of(person));

        TravelCalculatePremiumCoreCommand command = new TravelCalculatePremiumCoreCommand();
        command.setAgreement(agreement);

        when(validator.validate(agreement)).thenReturn(List.of());
        when(totalPremiumCalculator.calculate(agreement)).thenReturn(BigDecimal.valueOf(5));

        AgreementEntity mockEntity = new AgreementEntity();
        mockEntity.setUuid("test-uuid-123");
        when(entityFactory.createAgreementEntity(agreement)).thenReturn(mockEntity);

        TravelCalculatePremiumCoreResult result = service.calculatePremium(command);

        assertNotNull(result.getAgreement());
        assertEquals(BigDecimal.valueOf(5), result.getAgreement().getAgreementPremium());

        verify(personsPremiumCalculator).calculateRiskPremiums(agreement);
        verify(totalPremiumCalculator).calculate(agreement);
        verify(entityFactory).createAgreementEntity(agreement);
    }


    @Test
    void shouldCalculateTotalPremiumFromMultiplePersons() {
        PersonDTO person1 = new PersonDTO();
        PersonDTO person2 = new PersonDTO();

        AgreementDTO agreement = new AgreementDTO();
        agreement.setPersons(List.of(person1, person2));

        TravelCalculatePremiumCoreCommand command = new TravelCalculatePremiumCoreCommand();
        command.setAgreement(agreement);

        when(validator.validate(agreement)).thenReturn(List.of());
        when(totalPremiumCalculator.calculate(agreement)).thenReturn(BigDecimal.valueOf(12));

        AgreementEntity mockEntity = new AgreementEntity();
        mockEntity.setUuid("test-uuid-123");
        when(entityFactory.createAgreementEntity(agreement)).thenReturn(mockEntity);

        TravelCalculatePremiumCoreResult result = service.calculatePremium(command);

        assertNotNull(result.getAgreement());
        assertEquals(BigDecimal.valueOf(12), result.getAgreement().getAgreementPremium());

        verify(personsPremiumCalculator).calculateRiskPremiums(agreement);
        verify(totalPremiumCalculator).calculate(agreement);
        verify(entityFactory).createAgreementEntity(agreement);
    }
}
