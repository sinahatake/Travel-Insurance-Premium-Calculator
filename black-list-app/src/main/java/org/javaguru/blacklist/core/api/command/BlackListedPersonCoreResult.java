package org.javaguru.blacklist.core.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javaguru.blacklist.core.api.dto.BlackListedPersonDTO;
import org.javaguru.blacklist.core.api.dto.ValidationErrorDTO;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlackListedPersonCoreResult {

    private List<ValidationErrorDTO> errors;

    private BlackListedPersonDTO personDTO;

    public BlackListedPersonCoreResult(List<ValidationErrorDTO> errors) {
        this.errors = errors;
    }

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

}
