package org.javaguru.travel.insurance.core.messagebroker.proposal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaguru.travel.insurance.core.api.dto.AgreementDTO;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static org.javaguru.travel.insurance.core.messagebroker.RabbitMQConfig.QUEUE_PROPOSAL_GENERATION;

@Component
@Slf4j
@Profile("mysql-container")
@RequiredArgsConstructor
public class ProposalGeneratorQueueSenderImpl implements ProposalGeneratorQueueSender {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void send(AgreementDTO agreement) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String json = objectMapper.writeValueAsString(agreement);
            rabbitTemplate.convertAndSend(QUEUE_PROPOSAL_GENERATION, json);
            log.info("PROPOSAL GENERATION message content: {}", json);
        } catch (JsonProcessingException e) {
            log.error("Error to convert agreement to JSON", e);
        } catch (AmqpException e) {
            log.error("Error to sent proposal generation message", e);
        }
    }
}
