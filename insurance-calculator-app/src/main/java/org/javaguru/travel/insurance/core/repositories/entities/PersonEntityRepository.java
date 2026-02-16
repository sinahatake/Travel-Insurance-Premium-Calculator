package org.javaguru.travel.insurance.core.repositories.entities;

import org.javaguru.travel.insurance.core.domain.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonEntityRepository extends JpaRepository<PersonEntity, Long> {
    Optional<PersonEntity> findByFirstNameAndLastNameAndPersonCode(String firstName, String lastName, String personCode);
}
