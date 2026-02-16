package org.javaguru.travel.insurance.rest.v2;

import com.google.common.base.Stopwatch;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreCommand;
import org.javaguru.travel.insurance.core.api.command.TravelCalculatePremiumCoreResult;
import org.javaguru.travel.insurance.core.services.TravelCalculatePremiumService;
import org.javaguru.travel.insurance.dto.v2.DtoV2Converter;
import org.javaguru.travel.insurance.dto.v2.TravelCalculatePremiumRequestV2;
import org.javaguru.travel.insurance.dto.v2.TravelCalculatePremiumResponseV2;
import org.javaguru.travel.insurance.rest.common.TravelCalculatePremiumRequestExecutionTimeLogger;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insurance/travel/api/v2")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TravelCalculatePremiumControllerV2 {

    private final TravelCalculatePremiumService calculatePremiumService;
    private final TravelCalculatePremiumRequestLoggerV2 travelCalculatePremiumRequestLogger;
    private final TravelCalculatePremiumResponseLoggerV2 travelCalculatePremiumResponseLogger;
    private final TravelCalculatePremiumRequestExecutionTimeLogger travelCalculatePremiumRequestExecutionTimeLogger;
    private final DtoV2Converter dtoV2Converter;

    @PostMapping(path = "/",
            consumes = "application/json",
            produces = "application/json")

    public TravelCalculatePremiumResponseV2 calculatePremium(@RequestBody TravelCalculatePremiumRequestV2 request) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        TravelCalculatePremiumResponseV2 response = processRequest(request);
        travelCalculatePremiumRequestExecutionTimeLogger.logExecutionTime(stopwatch);
        return response;
    }

    private TravelCalculatePremiumResponseV2 processRequest(TravelCalculatePremiumRequestV2 request) {
        travelCalculatePremiumRequestLogger.log(request);
        TravelCalculatePremiumCoreCommand command = dtoV2Converter.buildCoreCommand(request);
        TravelCalculatePremiumCoreResult result = calculatePremiumService.calculatePremium(command);
        TravelCalculatePremiumResponseV2 response = dtoV2Converter.buildResponse(result);

        travelCalculatePremiumResponseLogger.log(response);
        return response;
    }

}