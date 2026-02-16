package org.javaguru.travel.insurance.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "medical_risk_limit_level")
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRiskLimitLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "MEDICAL_RISK_LIMIT_LEVEL_IC", length = 50)
    private String medicalRiskLimitLevelIc;

    @Column(name = "COEFFICIENT", nullable = false, precision = 10, scale = 2)
    private BigDecimal coefficient;

}

