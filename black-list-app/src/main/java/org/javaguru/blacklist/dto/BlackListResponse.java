package org.javaguru.blacklist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javaguru.blacklist.dto.CoreResponse;
import org.javaguru.blacklist.dto.ValidationError;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlackListResponse extends CoreResponse {

    private String personCode;
    private String personFirstName;
    private String personLastName;
    private Boolean blacklisted;

    public BlackListResponse(List<ValidationError> errors) {
        super(errors);
    }

}
