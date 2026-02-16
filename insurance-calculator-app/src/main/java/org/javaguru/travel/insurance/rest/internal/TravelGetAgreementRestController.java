package org.javaguru.travel.insurance.rest.internal;

import com.google.common.base.Stopwatch;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelGetAgreementCoreResult;
import org.javaguru.travel.insurance.core.services.TravelGetAgreementService;
import org.javaguru.travel.insurance.dto.internal.DtoInternalConverter;
import org.javaguru.travel.insurance.dto.internal.TravelGetAgreementResponse;
import org.javaguru.travel.insurance.rest.common.TravelCalculatePremiumRequestExecutionTimeLogger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insurance/travel/api/internal/agreement")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TravelGetAgreementRestController {

    private final TravelCalculatePremiumRequestExecutionTimeLogger travelCalculatePremiumRequestExecutionTimeLogger;
    private final TravelGetAgreementResponseLogger responseLogger;
    private final TravelGetAgreementRequestLogger requestLogger;
    private final TravelGetAgreementService travelGetAgreementService;
    private final DtoInternalConverter dtoInternalConverter;

    @GetMapping(path = "/{uuid}",
            produces = "application/json")
    public TravelGetAgreementResponse getAgreement(@PathVariable String uuid) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        TravelGetAgreementResponse response = processRequest(uuid);
        travelCalculatePremiumRequestExecutionTimeLogger.logExecutionTime(stopwatch);
        return response;
    }

    private TravelGetAgreementResponse processRequest(String uuid) {
        requestLogger.log(uuid);
        TravelGetAgreementCoreCommand command = dtoInternalConverter.buildCoreCommand(uuid);
        TravelGetAgreementCoreResult result = travelGetAgreementService.getAgreement(command);
        TravelGetAgreementResponse response = dtoInternalConverter.buildResponse(result);
        responseLogger.log(response);
        return response;
    }
}
