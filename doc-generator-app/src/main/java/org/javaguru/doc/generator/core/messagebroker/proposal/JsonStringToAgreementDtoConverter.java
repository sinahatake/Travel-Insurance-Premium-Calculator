package org.javaguru.doc.generator.core.messagebroker.proposal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.javaguru.doc.generator.core.api.dto.AgreementDTO;
import org.springframework.stereotype.Component;

@Component
class JsonStringToAgreementDtoConverter {

    public AgreementDTO convert(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(json, AgreementDTO.class);
    }

}
