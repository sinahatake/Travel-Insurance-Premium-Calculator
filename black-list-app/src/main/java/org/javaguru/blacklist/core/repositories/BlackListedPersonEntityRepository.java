package org.javaguru.blacklist.core.repositories;

import org.javaguru.blacklist.core.domain.BlackListedPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlackListedPersonEntityRepository extends JpaRepository<BlackListedPersonEntity, Long> {

    Optional<BlackListedPersonEntity> findByFirstNameAndLastNameAndPersonCode(String firstName, String lastName, String personCode);

}
