package org.openbakery.racecontrol.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.openbakery.jinsim.Tiny;
import org.openbakery.jinsim.response.InSimResponse;
import org.openbakery.jinsim.response.RaceStartResponse;
import org.openbakery.jinsim.response.ReorderResponse;
import org.openbakery.jinsim.response.ResultResponse;
import org.openbakery.jinsim.response.TinyResponse;

import org.openbakery.racecontrol.DriverNotFoundException;
import org.openbakery.racecontrol.Race;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.ResultComparator;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.data.RaceEntry;
import org.openbakery.racecontrol.data.Result;
import org.openbakery.racecontrol.data.Weather;
import org.openbakery.racecontrol.data.Wind;
import org.openbakery.racecontrol.event.RaceEvent;
import org.openbakery.racecontrol.event.RaceEvent.Type;
import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainRaceControl extends AbstractControl {

	private static Logger log = LoggerFactory.getLogger(MainRaceControl.class);

	public MainRaceControl(RaceControl raceControl, Persistence persistence) {
		super(raceControl, persistence);
	}

	public void packetReceived(InSimResponse response) {
		try {
			if (response instanceof TinyResponse) {
				TinyResponse tinyResponse = (TinyResponse) response;
				Tiny type = tinyResponse.getType();
				if (type == Tiny.MULTIPLAYER_END || type == Tiny.RACE_END) {
					endRace();
				}
			} else if (response instanceof ReorderResponse) {
				processReorderResponse((ReorderResponse) response);
			} else if (response instanceof RaceStartResponse) {
				processRaceStartResponse((RaceStartResponse) response);
			} else if (response instanceof ResultResponse) {
				processResultResponse((ResultResponse) response);

			}
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

	}

	private void endRace() throws PersistenceException {
		Race race = raceControl.getRace();
		// if (race.hasRaceEntry() && race.getRaceEntry().getResults().size() > 0) {
		calculate();
		raceControl.getRace().reset();
		raceControl.notifyRaceEventListener(new RaceEvent(Type.END, raceControl.getRace(), null));
		persistence.flush();
		log.info("Race has ended");
		// }
	}

	private void processRaceStartResponse(RaceStartResponse response) throws IOException {
		log.debug("start race: " + response);
		RaceEntry raceEntry = raceControl.getRace().getRaceEntry();
		raceEntry.setLaps(response.getRaceLaps());
		raceEntry.setHours(response.getRaceHours());
		raceEntry.setQualifyingMinutes(response.getQualifingMinutes());
		raceEntry.setRacers(response.getNumberOfPlayers());
		raceEntry.setTrack(response.getTrack().getShortname());
		raceEntry.setWeather(getWeather(response.getWeather()));
		raceEntry.setWind(getWind(response.getWind()));
		log.info("Race start...");

		try {
			RaceEntry newRaceEntry = persistence.store(raceEntry);
			//raceControl.getRace().setRaceEntry(newRaceEntry);
			raceEntry.setId(newRaceEntry.getId());
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}

		raceControl.notifyRaceEventListener(new RaceEvent(Type.STARTED, raceControl.getRace(), null));
		log.debug("{}", raceEntry);
		log.debug("with drivers {}", raceEntry.getDrivers());
	}

	private Wind getWind(org.openbakery.jinsim.Wind wind) {
		int windValue = org.openbakery.jinsim.Wind.getValue(wind);
		return Wind.getWind(windValue);
	}

	private Weather getWeather(org.openbakery.jinsim.Weather weather) {
		int weatherValue = org.openbakery.jinsim.Weather.getValue(weather);
		return Weather.getWeather(weatherValue);
	}

	private void processReorderResponse(ReorderResponse response) throws CloneNotSupportedException, PersistenceException {
		// endRace();
		Race race = raceControl.getRace();
		log.debug("processReorderResponse: current race drivers {}", raceControl.getRace().getRaceDrivers());

		int i = 1;
		for (int playerId : response.getPlayerPositions()) {
			if (playerId != 0) {
				Driver driver = race.getDriverByPlayerId(playerId);
				if (driver != null) {
					driver = driver.clone();
					driver.setStartingPosition(i++);
					Driver newDriver = persistence.store(driver);
					driver.setId(newDriver.getId());

					race.addRaceDriver(driver);
				}
			}
		}
		log.debug("race drivers: " + race.getRaceDrivers());
	}

	private void processResultResponse(ResultResponse response) {
		Race race = raceControl.getRace();
		try {
			Driver driver = race.getRaceDriver(response);
			if (driver == null) {
				log.error("Driver not found with id: " + response.getPlayerId());
			}

			Result result = new Result();
			result.setBestLapTime(response.getBestLapTime().getTime());
			result.setConfirmationFlags(response.getConfirmationFlags());
			result.setLapsCompleted(response.getLapsDone());
			result.setPitStops(response.getNumberPitStops());
			result.setRaceTime(response.getTotalTime().getTime());
			result.setPosition(response.getResultPosition() + 1);
			race.getRaceEntry().addResult(result, driver);
			// driver.setResult(result);
			try {
				Result newResult = persistence.store(result);
				result.setId(newResult.getId());
			} catch (PersistenceException e) {
				log.error("unable to store result: " + result);
			}
			try {
				if (driver.getPlayerId() > 0) {
					persistence.store(driver);
				}
			} catch (PersistenceException e) {
				log.error("unable to store driver: " + driver);
			}
			log.info("Result: " + result.toString(driver.getName()));

		} catch (DriverNotFoundException e) {
			log.error("Driver not found", e);
		}
	}

	public void calculate() throws PersistenceException {
		Race race = raceControl.getRace();
		if (race.hasRaceEntry()) {
			if (!race.getRaceEntry().isQualifying()) {
				calculatePositionInLaps();
				addUnfinishedRacers();
			}
		}
	}

	private void addUnfinishedRacers() throws PersistenceException {
		ArrayList<Result> allResults = new ArrayList<Result>();
		Race race = raceControl.getRace();
		for (Driver driver : race.getRaceDrivers()) {

			Result result = driver.getResult();
			int longestAttempt = driver.getLongestAttempt();

			if (result == null || result.getLapsCompleted() < driver.getCompletedLaps(longestAttempt).size()) {
				if (log.isDebugEnabled()) {
					log.debug("add unfinished racer: " + driver);
				}
				long bestLapTime = Long.MAX_VALUE;
				int completedLaps = 0;
				int pitStops = 0;
				long overallTime = 0;
				for (Lap lap : driver.getCompletedLaps(longestAttempt)) {
					if (lap.getTime() < bestLapTime) {
						bestLapTime = lap.getTime();
					}
					completedLaps++;
					overallTime = lap.getTotalTime();
				}
				if (overallTime != 0) {
					if (result == null) {
						result = new Result();
						result.setConfirmationFlags(128);
					}
					result.setBestLapTime(bestLapTime);
					result.setLapsCompleted(completedLaps);
					result.setPitStops(pitStops);
					result.setRaceTime(overallTime);
					result.setPosition(0);
					driver.setResult(result);

					Result newResult = persistence.store(result);
					result.setId(newResult.getId());
					persistence.store(driver);
				}

			}
			if (driver.getResult() != null) {
				allResults.add(driver.getResult());
			} else {
				log.info("No Result for driver: " + driver);
			}

		}

		if (allResults.size() > 0) {
			Collections.sort(allResults, new ResultComparator());
			for (int i = 0; i < allResults.size(); i++) {
				Result result = allResults.get(i);
                result.setPosition(i + 1);
                persistence.store(result);
			}
		}

	}

	private void calculatePositionInLaps() {
		ArrayList<Lap> previousLaps = null;
		ArrayList<Lap> currentLaps = null;
		ArrayList<Lap> positionList = new ArrayList<Lap>();
		Race race = raceControl.getRace();
        log.debug("race {}", race);
		for (int i = 0; i < race.getRaceEntry().getLaps(); i++) {
			currentLaps = new ArrayList<Lap>();
			previousLaps = new ArrayList<Lap>();
			positionList.clear();

			for (Driver driver : race.getRaceDrivers()) {
				// log.debug(driver);
				Lap lap = driver.getCompletedLap(i);
                log.debug("lap {}: {}", i, lap);
                if (lap != null) {
					currentLaps.add(lap);
					if (i > 0) {
						previousLaps.add(driver.getCompletedLap(i - 1));
					}
				}
			}

			if (previousLaps.size() == 0) {
				for (int j = 0; j < currentLaps.size(); j++) {
					Lap currentLap = currentLaps.get(j);
					positionList.add(currentLap);
				}
			} else {
				for (int j = 0; j < currentLaps.size(); j++) {
					Lap currentLap = currentLaps.get(j);
					if (currentLap.isFinished()) {
						positionList.add(currentLap);
					}
				}
			}

			Collections.sort(positionList);

			for (int j = 1; j <= positionList.size(); j++) {
                Lap lap = positionList.get(j - 1);
				lap.setPosition(j);
                try {
                    persistence.store(lap);
                } catch (PersistenceException e) {
                    log.error(e.getMessage(), e);
                }
            }
		}
	}

	@Override
	public void destroy() {
		try {
			endRace();
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}
	}
}
