package org.javaguru.travel.insurance.core.messagebroker.proposalAck;
import lombok.extern.slf4j.Slf4j;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.domain.entities.AgreementProposalAckEntity;
import org.javaguru.travel.insurance.core.repositories.entities.AgreementProposalAckEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ProposalGenerationAckService {

    private final AgreementProposalAckEntityRepository proposalAckEntityRepository;

    public void process(ProposalGenerationAck proposalGenerationAck) {
        log.info("Start to process proposal ack: {}", proposalGenerationAck.getAgreementUuid());

        AgreementProposalAckEntity ack = new AgreementProposalAckEntity();
        ack.setAgreementUuid(proposalGenerationAck.getAgreementUuid());
        ack.setAlreadyGenerated(true);
        ack.setProposalFilePath(proposalGenerationAck.getProposalFilePath());

        proposalAckEntityRepository.save(ack);

        log.info("Finish to process proposal ack: {}", proposalGenerationAck.getAgreementUuid());
    }

}
