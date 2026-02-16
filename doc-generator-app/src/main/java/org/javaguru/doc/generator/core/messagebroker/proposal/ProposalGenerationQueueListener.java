package org.javaguru.doc.generator.core.messagebroker.proposal;

import lombok.extern.slf4j.Slf4j;
import org.javaguru.doc.generator.core.api.dto.AgreementDTO;
import org.javaguru.doc.generator.core.messagebroker.RabbitMQConfig;
import org.javaguru.doc.generator.core.messagebroker.proposalAck.ProposalGenerationAckQueueSender;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProposalGenerationQueueListener {

    private final Integer totalRetryCount;

    private final JsonStringToAgreementDtoConverter agreementDtoConverter;
    private final ProposalGenerator proposalGenerator;
    private final RabbitTemplate rabbitTemplate;
    private final ProposalGenerationAckQueueSender proposalGenerationAckQueueSender;

    ProposalGenerationQueueListener(@Value("${rabbitmq.total.retry.count:3}")
                                    Integer totalRetryCount,
                                    JsonStringToAgreementDtoConverter agreementDtoConverter,
                                    ProposalGenerator proposalGenerator,
                                    RabbitTemplate rabbitTemplate, ProposalGenerationAckQueueSender proposalGenerationAckQueueSender) {
        this.totalRetryCount = totalRetryCount;
        this.agreementDtoConverter = agreementDtoConverter;
        this.proposalGenerator = proposalGenerator;
        this.rabbitTemplate = rabbitTemplate;
        this.proposalGenerationAckQueueSender = proposalGenerationAckQueueSender;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROPOSAL_GENERATION)
    public void receiveMessage(final Message message) throws Exception {
        try {
            processMessage(message);
        } catch (Exception e) {
            log.error("FAIL to process message: ", e);
            retryOrForwardToDeadLetterQueue(message);
        }
    }

    private void retryOrForwardToDeadLetterQueue(Message message) {
        Integer retryCount = message.getMessageProperties().getHeader("x-retry-count");
        log.info("MESSAGE DELIVERY TAG "
                + message.getMessageProperties().getDeliveryTag()
                + " RETRY COUNT = " + retryCount);
        if (retryCount == null) {
            retryCount = 0;
        }
        retryCount++;
        if (retryCount <= totalRetryCount) {
            // Update retry count and republish for retry
            message.getMessageProperties().setHeader("x-retry-count", retryCount);
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION, message);
        } else {
            // Forward to DLQ
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_DLQ, message);
        }
    }

    private void processMessage(Message message) throws Exception {
        String messageBody = new String(message.getBody());
        log.info(messageBody);
        AgreementDTO agreementDTO = agreementDtoConverter.convert(messageBody);
        String filepath = proposalGenerator.generateProposalAndStoreToFile(agreementDTO);
        proposalGenerationAckQueueSender.send(agreementDTO.getUuid(), filepath);

    }
}
