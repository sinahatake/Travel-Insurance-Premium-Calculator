package org.javaguru.travel.insurance.core.blacklist;

import org.javaguru.travel.insurance.core.api.dto.PersonDTO;

public interface BlackListedPersonService {

    Boolean isBlacklisted(PersonDTO person);

}