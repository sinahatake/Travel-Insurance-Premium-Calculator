package org.javaguru.travel.insurance.core.repositories;

import org.javaguru.travel.insurance.core.domain.ClassifierValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class ClassifierValueRepositoryTest {

    @Autowired private ClassifierValueRepository classifierValueRepository;

    @Test
    public void injectedRepositoryAreNotNull() {
        assertNotNull(classifierValueRepository);
    }

    @Test
    public void shouldNotFind_RiskType_FAKE() {
        Optional<ClassifierValue> valueOpt = classifierValueRepository.findByClassifierTitleAndIc("RISK_TYPE", "FAKE");
        assertTrue(valueOpt.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("riskTypeValues")
    public void shouldFindRiskTypeValueByIc(String ic) {
        Optional<ClassifierValue> valueOpt = classifierValueRepository.findByClassifierTitleAndIc("RISK_TYPE", ic);
        assertTrue(valueOpt.isPresent());
        assertEquals(valueOpt.get().getIc(), ic);
        assertEquals(valueOpt.get().getClassifier().getTitle(), "RISK_TYPE");
    }

    private static List<String> riskTypeValues() {
        return List.of(
                "TRAVEL_MEDICAL",
                "TRAVEL_CANCELLATION",
                "TRAVEL_LOSS_BAGGAGE",
                "TRAVEL_THIRD_PARTY_LIABILITY",
                "TRAVEL_EVACUATION",
                "TRAVEL_SPORT_ACTIVITIES"
        );
    }


}