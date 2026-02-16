package org.javaguru.doc.generator.core.messagebroker.proposalAck;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaguru.doc.generator.core.api.dto.ProposalGenerationAcknowledgment;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static org.javaguru.doc.generator.core.messagebroker.RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK;


@Component
@Slf4j
@RequiredArgsConstructor
public class ProposalGenerationAckQueueSenderImpl implements ProposalGenerationAckQueueSender {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void send(String uuid, String filepath) {
        ProposalGenerationAcknowledgment ack = new ProposalGenerationAcknowledgment();
        ack.setAgreementUuid(uuid);
        ack.setProposalFilePath(filepath);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(ack);
            rabbitTemplate.convertAndSend(QUEUE_PROPOSAL_GENERATION_ACK, json);
            log.info("PROPOSAL GENERATION message content: {}", json);
        } catch (JsonProcessingException e) {
            log.error("Error to convert agreement to JSON", e);
        } catch (AmqpException e) {
            log.error("Error to sent proposal generation message", e);
        }
    }

}
