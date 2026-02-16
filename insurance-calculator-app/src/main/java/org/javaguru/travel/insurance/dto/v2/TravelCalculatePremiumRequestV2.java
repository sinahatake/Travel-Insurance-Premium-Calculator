package org.javaguru.travel.insurance.dto.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelCalculatePremiumRequestV2 {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate agreementDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate agreementDateTo;

    private String country;

    @JsonProperty("risks")
    private List<String> selectedRisks;

    private List<PersonRequestDTO> persons;

}
