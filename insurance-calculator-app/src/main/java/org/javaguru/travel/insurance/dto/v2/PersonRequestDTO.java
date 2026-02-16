package org.javaguru.travel.insurance.dto.v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonRequestDTO {
    private String personCode;
    private String personFirstName;
    private String personLastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate PersonBirthDate;

    private String medicalRiskLimitLevel;

}
