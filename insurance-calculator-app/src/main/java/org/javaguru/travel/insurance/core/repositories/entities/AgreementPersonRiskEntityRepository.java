package org.javaguru.travel.insurance.core.repositories.entities;

import org.javaguru.travel.insurance.core.domain.entities.AgreementPersonEntity;
import org.javaguru.travel.insurance.core.domain.entities.AgreementPersonRiskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreementPersonRiskEntityRepository extends JpaRepository<AgreementPersonRiskEntity, Integer> {
    Optional<AgreementPersonRiskEntity> findByAgreementPerson(AgreementPersonEntity agreementPerson);
}
