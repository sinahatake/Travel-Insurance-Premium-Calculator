package org.javaguru.travel.insurance.core.blacklist.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackListRequest {
    private String personCode;
    private String personFirstName;
    private String personLastName;

}
