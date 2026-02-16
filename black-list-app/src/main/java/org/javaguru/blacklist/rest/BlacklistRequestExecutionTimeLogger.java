package org.javaguru.blacklist.rest;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BlacklistRequestExecutionTimeLogger {

    private static final Logger logger = LoggerFactory.getLogger(BlacklistRequestExecutionTimeLogger.class);

    public void logExecutionTime(Stopwatch stopwatch) {
        stopwatch.stop();
        long elapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        logger.info("Request processing time: {} ms", elapsedTime);
    }
}
