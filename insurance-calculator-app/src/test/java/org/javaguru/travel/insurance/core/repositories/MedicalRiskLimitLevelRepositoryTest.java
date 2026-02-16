package org.javaguru.travel.insurance.core.repositories;

import org.javaguru.travel.insurance.core.domain.MedicalRiskLimitLevel;
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
class MedicalRiskLimitLevelRepositoryTest {

    @Autowired
    private MedicalRiskLimitLevelRepository repository;

    @Test
    public void injectedRepositoryAreNotNull() {
        assertNotNull(repository);
    }

    @Test
    public void shouldFindLevel10000() {
        searchMedicalRiskLimitLevel("LEVEL_10000", new BigDecimal("1.00"));
    }

    @Test
    public void shouldFindLevel15000() {
        searchMedicalRiskLimitLevel("LEVEL_15000", new BigDecimal("1.20"));
    }

    @Test
    public void shouldFindLevel20000() {
        searchMedicalRiskLimitLevel("LEVEL_20000", new BigDecimal("1.50"));
    }

    @Test
    public void shouldFindLevel50000() {
        searchMedicalRiskLimitLevel("LEVEL_50000", new BigDecimal("2.00"));
    }

    @Test
    public void shouldNotFindUnknownLevel() {
        Optional<MedicalRiskLimitLevel> result = repository.findByMedicalRiskLimitLevelIc("FAKE_LEVEL");
        assertTrue(result.isEmpty());
    }

    private void searchMedicalRiskLimitLevel(String ic, BigDecimal expectedCoefficient) {
        Optional<MedicalRiskLimitLevel> result = repository.findByMedicalRiskLimitLevelIc(ic);
        assertTrue(result.isPresent());
        assertEquals(ic, result.get().getMedicalRiskLimitLevelIc());
        assertEquals(expectedCoefficient.stripTrailingZeros(), result.get().getCoefficient().stripTrailingZeros());
    }
}
