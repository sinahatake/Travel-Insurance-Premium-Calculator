package org.javaguru.travel.insurance.rest.common;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TravelCalculatePremiumRequestExecutionTimeLogger {

    private static final Logger logger =  LoggerFactory.getLogger(TravelCalculatePremiumRequestExecutionTimeLogger.class);

    public void logExecutionTime(Stopwatch stopwatch){
        stopwatch.stop();
        long elapsedTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        logger.info("Request processing time: " + elapsedTime + " ms");
    }
}
