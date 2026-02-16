package org.javaguru.blacklist.core.validations;


import org.javaguru.blacklist.core.api.dto.BlackListedPersonDTO;
import org.javaguru.blacklist.core.api.dto.ValidationErrorDTO;

import java.util.Optional;

public interface BlackListRequestValidation {

    Optional<ValidationErrorDTO> validate(BlackListedPersonDTO blackListedPersonDTO);

}
