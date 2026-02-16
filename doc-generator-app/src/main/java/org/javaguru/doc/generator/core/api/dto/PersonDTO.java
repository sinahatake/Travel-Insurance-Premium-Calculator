package org.javaguru.doc.generator.core.api.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private String personFirstName;

    private String personLastName;

    private String personCode;

    private LocalDate personBirthDate;

    private String medicalRiskLimitLevel;

    private List<RiskDTO> risks;

}
