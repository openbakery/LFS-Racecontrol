package org.openbakery.racecontrol.control;

import net.sf.jinsim.response.InSimResponse;
import net.sf.jinsim.response.LapTimeResponse;
import net.sf.jinsim.response.SplitTimeResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openbakery.racecontrol.DriverNotFoundException;
import org.openbakery.racecontrol.Race;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.event.LapEvent;
import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create the lap object and track the lap times and split times
 * 
 * @author rene
 * 
 */
public class LapControl extends AbstractControl {

  private static Logger log = LoggerFactory.getLogger(LapControl.class);


	public LapControl(RaceControl raceControl, Persistence persistence) {
		super(raceControl, persistence);
	}

	public void packetReceived(InSimResponse response) {
		try {
			if (response instanceof LapTimeResponse) {
				processLapTimeResponse((LapTimeResponse) response);
			} else if (response instanceof SplitTimeResponse) {
				processSplitTimeResponse((SplitTimeResponse) response);
			}
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalStateException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void processLapTimeResponse(LapTimeResponse response) throws PersistenceException {
		Race race = raceControl.getRace();
		if (race.hasRaceEntry()) {
			Driver driver;
			try {
				driver = race.getRaceDriver(response);
				if (!race.hasFinished(driver)) {
					Lap lap = driver.getCurrentLap();
					lap.setTime(response.getTime().getTime());
					lap.setTotalTime(response.getTotalTime().getTime());
					lap.setPenalty(response.getPenalty());
					driver.addCompletedLap(lap);
					Lap newLap = persistence.store(lap);
					lap.setId(newLap.getId());
					// persistence.store(driver);
					log.info("Lap: " + lap.toString());
					raceControl.notifyLapEventListener(new LapEvent(driver, lap));

					// if race duration is in hours then increase the racelaps
					if (raceControl.getRace().getRaceEntry().getHours() != 0) {
						if (lap.getNumber() > raceControl.getRace().getRaceEntry().getLaps()) {
							raceControl.getRace().getRaceEntry().setLaps(lap.getNumber());
						}
					}

				}
			} catch (DriverNotFoundException e) {
				log.warn(e.getMessage());
			}
		} else {
			if (log.isDebugEnabled())
				log.debug("waiting for race to start...");
		}

	}

	private void processSplitTimeResponse(SplitTimeResponse response) {
		Race race = raceControl.getRace();
		if (race.hasRaceEntry()) {
			Driver driver;
			try {
				driver = race.getRaceDriver(response);
				if (!race.hasFinished(driver)) {
					Lap lap = driver.getCurrentLap();
					/*
					 * if (log.isDebugEnabled()) { log.debugexit ("time: " + response.getTime()); }
					 */
					lap.addSplit(response.getSplit(), response.getTime().getTime());
					// if (log.isDebugEnabled()) {
					// log.debug("Split: " + lap);
					// }
					raceControl.notifyLapEventListener(new LapEvent(driver, lap, response.getSplit()));
				}
			} catch (DriverNotFoundException ex) {
				log.warn(ex.getMessage());
			}
		} else {
			if (log.isDebugEnabled())
				log.debug("waiting for race to start...");
		}
	}

	@Override
	public void destroy() {
	}

}
