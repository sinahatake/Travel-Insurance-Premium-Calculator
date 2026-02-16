package org.javaguru.travel.insurance.core.messagebroker;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PROPOSAL_GENERATION = "q.proposal-generation";
    public static final String QUEUE_PROPOSAL_GENERATION_DLQ = "q.proposal-generation-dlq";
    public static final String QUEUE_PROPOSAL_GENERATION_ACK = "q.proposal-generation-ack";
    public static final String QUEUE_PROPOSAL_GENERATION_ACK_DLQ = "q.proposal-generation-ack-dlq";

    @Bean
    public Queue proposalPdfGenerationQueue() {
        return new Queue(QUEUE_PROPOSAL_GENERATION);
    }

    @Bean
    public Queue proposalPdfGenerationDeadLetterQueue() {
        return new Queue(QUEUE_PROPOSAL_GENERATION_DLQ);
    }

    @Bean
    public Queue proposalGenerationAck() {
        return new Queue(QUEUE_PROPOSAL_GENERATION_ACK);
    }

    @Bean
    public Queue proposalGenerationAckDeadLetterQueue() {
        return new Queue(QUEUE_PROPOSAL_GENERATION_ACK_DLQ);
    }

}