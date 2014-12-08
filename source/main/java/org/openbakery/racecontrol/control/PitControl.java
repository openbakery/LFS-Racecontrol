package org.openbakery.racecontrol.control;

import org.openbakery.jinsim.response.InSimResponse;
import org.openbakery.jinsim.response.PitLaneResponse;
import org.openbakery.jinsim.response.PitStopFinishedResponse;
import org.openbakery.jinsim.response.PitStopResponse;
import org.openbakery.jinsim.response.PlayerPitsResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openbakery.racecontrol.DriverNotFoundException;
import org.openbakery.racecontrol.Race;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.data.Tyres;
import org.openbakery.racecontrol.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Track if a player pits or leaves the pits
 * @author rene
 *
 */
public class PitControl extends AbstractControl {
	
  private static Logger log = LoggerFactory.getLogger(PitControl.class);

	public PitControl(RaceControl raceControl, Persistence persistence) {
		super(raceControl, persistence);
	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof PlayerPitsResponse) {
    	processPlayerPitResponse((PlayerPitsResponse)response);
    } else if (response instanceof PitLaneResponse) {
    	processPitLaneResponse((PitLaneResponse)response);
    } else if (response instanceof PitStopResponse) {
    	processPitStopResponse((PitStopResponse)response);
    } else if (response instanceof PitStopFinishedResponse) {
    	processPitStopFinishedResponse((PitStopFinishedResponse)response);
    } 

	}

	
	private void processPlayerPitResponse(PlayerPitsResponse response) {
		Race race = raceControl.getRace();
		if (race.hasRaceEntry()) {
			Driver driver;
			try {
				driver = race.getRaceDriver(response);
				if (!race.hasFinished(driver)) {
					driver.addJoin();
				}
			} catch (DriverNotFoundException e) {
				log.warn("driver not found: " + response.getPlayerId(), e);
			}
		}
	}
	
	private void processPitLaneResponse(PitLaneResponse response) {
	}

	private void processPitStopFinishedResponse(PitStopFinishedResponse response) {
		Race race = raceControl.getRace();
		Driver driver;
		try {
			driver = race.getRaceDriver(response);
			if (!race.hasFinished(driver)) {
				Lap lap = driver.getCurrentLap();
				lap.setPitStopTime(response.getStopTime().getTime());
			}
		} catch (DriverNotFoundException e) {
			log.warn(e.getMessage());
		}
	}

	private void processPitStopResponse(PitStopResponse response) {
		Race race = raceControl.getRace();
		Driver driver;
		try {
			driver = race.getRaceDriver(response);
			if (!race.hasFinished(driver)) {
				Lap lap = driver.getCurrentLap();
				lap.setPenalty(response.getPenalty());
				lap.setNumberStops(response.getNumberOfPitstops());
				org.openbakery.jinsim.types.Tyres insimTyres = response.getTyres();
				lap.setTyres(new Tyres(insimTyres.getRearLeft(), insimTyres.getRearRight(), insimTyres.getFrontLeft(), insimTyres.getFrontRight()));
				lap.setWork(response.getWork());
				lap.setPit(true);
			}
		} catch (DriverNotFoundException e) {
			log.warn(e.getMessage());
		}
	}

	@Override
	public void destroy() {
	}

}
