package org.javaguru.blacklist.core.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlackListedPersonDTO {

    private String personCode;

    private String personFirstName;

    private String personLastName;

    private Boolean blacklisted;

    public boolean isBlackListed() {
        return blacklisted;
    }
}
