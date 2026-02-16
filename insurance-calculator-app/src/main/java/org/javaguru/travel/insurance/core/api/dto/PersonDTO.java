package org.javaguru.travel.insurance.core.api.dto;

import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javaguru.travel.insurance.jobs.LocalDateAdapter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonDTO {

    private String personCode;

    @Size(max = 200)
    private String personFirstName;

    @Size(max = 200)
    private String personLastName;

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate personBirthDate;

    private String medicalRiskLimitLevel;

    private List<RiskDTO> risks;


}
