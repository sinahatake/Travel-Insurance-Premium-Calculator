package org.javaguru.travel.insurance.rest.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TravelGetAgreementRequestLogger {
    void log(String uuid) {
        log.info("UUID: {}", uuid);
    }
}
