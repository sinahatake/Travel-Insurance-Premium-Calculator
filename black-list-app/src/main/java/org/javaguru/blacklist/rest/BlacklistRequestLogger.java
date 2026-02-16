package org.javaguru.blacklist.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.javaguru.blacklist.dto.BlackListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class BlacklistRequestLogger {

    private static final Logger logger = LoggerFactory.getLogger(BlacklistRequestLogger.class);

    void log(BlackListRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String json = objectMapper.writeValueAsString(request);
            logger.info("REQUEST: {}", json);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

}
