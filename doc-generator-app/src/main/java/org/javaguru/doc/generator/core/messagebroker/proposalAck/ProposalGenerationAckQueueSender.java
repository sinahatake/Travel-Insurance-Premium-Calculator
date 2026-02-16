package org.javaguru.doc.generator.core.messagebroker.proposalAck;

public interface ProposalGenerationAckQueueSender {
    void send(String uuid, String filepath);
}
