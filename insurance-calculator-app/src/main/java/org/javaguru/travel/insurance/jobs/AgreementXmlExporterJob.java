package org.javaguru.travel.insurance.jobs;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaguru.travel.insurance.core.api.command.TravelGetNotExportedAgreementUuidsCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetNotExportedAgreementUuidsCoreResult;
import org.javaguru.travel.insurance.core.services.TravelGetNotExportedAgreementUuidsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
@ConditionalOnProperty(
        name = "agreement.xml.exporter.job.enabled",
        havingValue = "true"
)
public class AgreementXmlExporterJob {

    @Value("${job.thread.pool.size}")
    private int poolSize;

    private final AgreementXmlExporter agreementXmlExporter;
    private final TravelGetNotExportedAgreementUuidsService travelGetNotExportedAgreementUuidsService;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void job() {
        log.info("Job started");
        loadAllAgreements();
        log.info("Job finished");

    }

    private List<String> getAllNotExportedUuids() {
        TravelGetNotExportedAgreementUuidsCoreCommand command = new TravelGetNotExportedAgreementUuidsCoreCommand();
        TravelGetNotExportedAgreementUuidsCoreResult result = travelGetNotExportedAgreementUuidsService.getNotExportedAgreement(command);
        return result.getUuids();
    }

    private void loadAllAgreements() {
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

        List<Future<?>> futures = getAllNotExportedUuids().stream()
                .map(uuid -> executorService.submit(() -> agreementXmlExporter.exportAgreement(uuid)))
                .collect(Collectors.toList());

        waitUntilAllTasksWillBeExecuted(futures);
        executorService.shutdown();
    }

    private void waitUntilAllTasksWillBeExecuted(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                log.error("Задача была прервана", e);
                Thread.currentThread().interrupt(); // корректная обработка прерывания
            } catch (ExecutionException e) {
                log.error("Ошибка при выполнении задачи", e.getCause());
            }
        }
    }


}