package org.javaguru.blacklist.rest;

import com.google.common.base.Stopwatch;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.javaguru.blacklist.core.api.command.BlackListedPersonCoreCommand;
import org.javaguru.blacklist.core.api.command.BlackListedPersonCoreResult;
import org.javaguru.blacklist.core.services.BlackListedPersonService;
import org.javaguru.blacklist.dto.BlackListRequest;
import org.javaguru.blacklist.dto.BlackListResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blacklist/person/check")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BlacklistController {

    private final BlackListedPersonService service;
    private final BlacklistRequestLogger requestLogger;
    private final BlacklistResponseLogger responseLogger;
    private final BlacklistRequestExecutionTimeLogger executionTimeLogger;
    private final DtoConverter converter;


    @PostMapping(path = "/",
            consumes = "application/json",
            produces = "application/json")

    public BlackListResponse check(@RequestBody BlackListRequest request) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        BlackListResponse response = processRequest(request);
        executionTimeLogger.logExecutionTime(stopwatch);
        return response;
    }

    private BlackListResponse processRequest(BlackListRequest request) {
        requestLogger.log(request);
        BlackListedPersonCoreCommand command = converter.buildCoreCommand(request);
        BlackListedPersonCoreResult result = service.check(command);
        BlackListResponse response = converter.buildResponse(result);

        responseLogger.log(response);
        return response;
    }


}