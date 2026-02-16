package org.javaguru.travel.insurance.core.repositories;

import org.javaguru.travel.insurance.core.domain.AgeCoefficient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AgeCoefficientRepository extends JpaRepository<AgeCoefficient, Long> {

    @Query("SELECT a FROM AgeCoefficient a " +
            "WHERE :age " +
            "BETWEEN a.ageFrom AND a.ageTo")
    Optional<AgeCoefficient> findCoefficientByAge(@Param("age") int age);

}
