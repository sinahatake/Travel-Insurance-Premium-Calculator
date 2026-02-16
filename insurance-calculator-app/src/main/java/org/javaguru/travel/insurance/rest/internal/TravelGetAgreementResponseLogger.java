package org.javaguru.travel.insurance.rest.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.javaguru.travel.insurance.dto.internal.TravelGetAgreementResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class TravelGetAgreementResponseLogger {
    void log(TravelGetAgreementResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String json = objectMapper.writeValueAsString(response);
            log.info("RESPONSE: {}", json);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}