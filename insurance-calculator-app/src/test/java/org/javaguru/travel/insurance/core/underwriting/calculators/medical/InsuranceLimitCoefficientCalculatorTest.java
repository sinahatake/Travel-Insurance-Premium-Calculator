package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.domain.MedicalRiskLimitLevel;
import org.javaguru.travel.insurance.core.repositories.MedicalRiskLimitLevelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsuranceLimitCoefficientCalculatorTest {

    @Mock
    private MedicalRiskLimitLevelRepository repository;

    @InjectMocks
    private InsuranceLimitCoefficientCalculator calculator;
    private PersonDTO person;

    @BeforeEach
    void setUp() {
        person = new PersonDTO();
        person.setMedicalRiskLimitLevel("LEVEL_20000");
        ReflectionTestUtils.setField(calculator, "medicalRiskLimitEnabled", true);
    }

    @Test
    @DisplayName("Should return correct coefficient for medical risk limit level")
    void shouldReturnCorrectCoefficient() {
        BigDecimal expectedCoefficient = new BigDecimal("1.5");

        MedicalRiskLimitLevel limitLevel = mock(MedicalRiskLimitLevel.class);
        when(limitLevel.getCoefficient()).thenReturn(expectedCoefficient);

        when(repository.findByMedicalRiskLimitLevelIc("LEVEL_20000"))
                .thenReturn(Optional.of(limitLevel));

        BigDecimal result = calculator.calculate(person);

        assertEquals(expectedCoefficient, result);
    }

    @Test
    @DisplayName("Should throw exception if medical risk limit level not found")
    void shouldThrowExceptionIfLimitLevelNotFound() {
        when(repository.findByMedicalRiskLimitLevelIc("LEVEL_20000"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> calculator.calculate(person));

        assertEquals("MedicalRiskLimitLevel not found: LEVEL_20000", exception.getMessage());
    }

    @Test
    @DisplayName("Should return default value when feature disabled")
    void shouldReturnDefaultValueWhenFeatureDisabled() {
        ReflectionTestUtils.setField(calculator, "medicalRiskLimitEnabled", false);

        BigDecimal result = calculator.calculate(person);

        assertEquals(BigDecimal.ONE, result);
    }
}
