package org.javaguru.travel.insurance.core.messagebroker.proposal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.springframework.amqp.AmqpException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile({"h2", "mysql-local"})
class ProposalGeneratorQueueSenderStubImpl implements ProposalGeneratorQueueSender {

    @Override
    public void send(AgreementDTO agreement) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String json = objectMapper.writeValueAsString(agreement);
            log.info("PROPOSAL GENERATION message content: {}", json);
        } catch (JsonProcessingException e) {
            log.error("Error to convert agreement to JSON", e);
        } catch (AmqpException e) {
            log.error("Error to sent proposal generation message", e);
        }
    }

}