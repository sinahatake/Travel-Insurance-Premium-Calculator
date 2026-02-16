package org.javaguru.blacklist.dto;

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
