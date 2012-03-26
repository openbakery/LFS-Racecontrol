package org.openbakery.racecontrol.persistence;

import org.openbakery.racecontrol.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rene
 * Date: 23.03.12
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class RCSVGenerator {
  public static final String DELIMITER = ";";
  private static Logger log = LoggerFactory.getLogger(RCSVGenerator.class);


    public String generate(RaceEntry raceEntry) {

        if (raceEntry == null) {
            throw new IllegalArgumentException("Given race is null");
        }
        StringWriter writer = new StringWriter();

        try {
            RaceEntry.storeHeader(writer);
            raceEntry.store(writer);

            Driver.storeHeader(writer);
            for (Driver driver : raceEntry.getDrivers()) {
                driver.store(writer);
            }

            Result.storeHeader(writer);
            for (Driver driver : raceEntry.getDrivers()) {
                Result result = driver.getResult();
                if (result != null)
                    result.store(driver.getName(), writer);
            }

            Lap.storeHeader(writer);
            for (Driver driver : raceEntry.getDrivers()) {
                log.debug("number of laps found: {}", driver.getCompletedLaps().size());
                log.debug("driver result: {}", driver.getResult());
                if (driver.getResult() != null) {
                    List<Lap> longestAttemptLaps = driver.getLongestAttemptLaps();
                    log.debug("longest attempt laps: {}", longestAttemptLaps.size());
                    for (Lap lap : longestAttemptLaps) {
                        lap.store(driver.getName(), writer);
                    }
                }
            }

            Flag.storeHeader(writer);
            for (Driver driver : raceEntry.getDrivers()) {
                Result result = driver.getResult();
                if (result != null) {
                    int numberLaps = result.getLapsCompleted();
                    for (Lap lap : driver.getAllLaps()) {
                        for (Flag flag : lap.getFlags()) {
                            if (lap.getNumber() <= numberLaps) {
                                flag.store(driver.getName(), lap.getNumber(), writer);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "";
        }

        return writer.toString();
    }

}
