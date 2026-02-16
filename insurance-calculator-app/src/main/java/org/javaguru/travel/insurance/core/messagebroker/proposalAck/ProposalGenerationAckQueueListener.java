package org.javaguru.travel.insurance.core.messagebroker.proposalAck;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.javaguru.travel.insurance.core.messagebroker.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProposalGenerationAckQueueListener {

    @Value("${rabbitmq.total.retry.count:3}")
    private Integer totalRetryCount;

    private final JsonStringToProposalGenerationAckConverter converter;

    private final RabbitTemplate rabbitTemplate;

    private final ProposalGenerationAckService proposalGenerationAckService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK)
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
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK, message);
        } else {
            // Forward to DLQ
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK_DLQ, message);
        }
    }

    private void processMessage(Message message) throws Exception {
        String messageBody = new String(message.getBody());
        log.info(messageBody);
        ProposalGenerationAck ack = converter.convert(messageBody);
        proposalGenerationAckService.process(ack);

    }
}
