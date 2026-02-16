package org.javaguru.travel.insurance.core.repositories;

import org.javaguru.travel.insurance.core.domain.AgeCoefficient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AgeCoefficientRepositoryTest {

    @Autowired
    private AgeCoefficientRepository ageCoefficientRepository;

    @Test
    @DisplayName("Injected repository is not null")
    void injectedRepositoryAreNotNull() {
        assertNotNull(ageCoefficientRepository);
    }

    @Test
    @DisplayName("Should find coefficient for age 5 (range 1..10)")
    void shouldFindCoefficientForAge5() {
        searchCoefficientByAge(5, new BigDecimal("1.10"));
    }

    @Test
    @DisplayName("Should find coefficient for age 17 (range 11..17)")
    void shouldFindCoefficientForAge17() {
        searchCoefficientByAge(17, new BigDecimal("1.00"));
    }

    @Test
    @DisplayName("Should find coefficient for age 41 (range 41..65)")
    void shouldFindCoefficientForAge41() {
        searchCoefficientByAge(41, new BigDecimal("1.20"));
    }

    @Test
    @DisplayName("Should NOT find coefficient for age 200 (out of all ranges)")
    void shouldNotFindCoefficientForAge200() {
        Optional<AgeCoefficient> result = ageCoefficientRepository.findCoefficientByAge(200);
        assertTrue(result.isEmpty());
    }

    private void searchCoefficientByAge(int age, BigDecimal expectedCoefficient) {
        Optional<AgeCoefficient> result = ageCoefficientRepository.findCoefficientByAge(age);
        assertTrue(result.isPresent(), "Coefficient should be found for age: " + age);
        assertEquals(
            expectedCoefficient.stripTrailingZeros(),
            result.get().getCoefficient().stripTrailingZeros()
        );
    }
}
