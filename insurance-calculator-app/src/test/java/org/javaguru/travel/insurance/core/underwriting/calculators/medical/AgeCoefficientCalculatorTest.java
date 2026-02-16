package org.javaguru.travel.insurance.core.underwriting.calculators.medical;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.javaguru.travel.insurance.core.domain.AgeCoefficient;
import org.javaguru.travel.insurance.core.repositories.AgeCoefficientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgeCoefficientCalculatorTest {

    @Mock
    private AgeCoefficientRepository repository;

    @InjectMocks
    private AgeCoefficientCalculator calculator;

    private PersonDTO person;

    @BeforeEach
    void setUp() {
        person = new PersonDTO();
        person.setPersonBirthDate(LocalDate.of(1990, 1, 1));

        ReflectionTestUtils.setField(calculator, "ageCoefficientEnabled", true);
    }

    @Test
    @DisplayName("Should return correct coefficient for age")
    void shouldReturnCorrectCoefficient() {
        int age = LocalDate.now().getYear() - 1990;
        BigDecimal expectedCoefficient = new BigDecimal("1.5");

        AgeCoefficient ageCoefficient = mock(AgeCoefficient.class);
        when(ageCoefficient.getCoefficient()).thenReturn(expectedCoefficient);
        when(repository.findCoefficientByAge(age)).thenReturn(Optional.of(ageCoefficient));

        BigDecimal result = calculator.calculate(person);

        assertEquals(expectedCoefficient, result);
    }

    @Test
    @DisplayName("Should return default coefficient when ageCoefficientEnabled is false")
    void shouldReturnDefaultCoefficient() {
        ReflectionTestUtils.setField(calculator, "ageCoefficientEnabled", false);

        BigDecimal result = calculator.calculate(person);

        assertEquals(BigDecimal.ONE, result);
    }

    @Test
    @DisplayName("Should throw exception if age coefficient not found")
    void shouldThrowExceptionIfAgeNotFound() {
        int age = LocalDate.now().getYear() - 1990;

        when(repository.findCoefficientByAge(age)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> calculator.calculate(person));

        assertEquals("Age not found: " + age, exception.getMessage());
    }
}
