package org.javaguru.travel.insurance.core.blacklist;

import lombok.extern.slf4j.Slf4j;
import org.javaguru.travel.insurance.core.api.dto.PersonDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile({"h2"})
class BlackListedPersonServiceStubImpl implements BlackListedPersonService {

    @Override
    public Boolean isBlacklisted(PersonDTO personDTO) {
        log.info("BlackList stub invoked! Always return false!");
        return false;
    }

}
