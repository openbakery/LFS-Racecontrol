package org.openbakery.racecontrol;

import java.util.*;

import org.openbakery.jinsim.response.PlayerResponse;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.RaceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Race {

	private static Logger log = LoggerFactory.getLogger(Race.class);

	private RaceEntry raceEntry;

	private ArrayList<String> messages;

	public Race() {
		messages = new ArrayList<String>();
	}

	public RaceEntry getRaceEntry() {
		if (raceEntry == null) {
			raceEntry = new RaceEntry();
		}
		return raceEntry;
	}

	public void setRaceEntry(RaceEntry raceEntry) {
		this.raceEntry = raceEntry;
	}



	public Driver getRaceDriver(PlayerResponse playerResponse) throws DriverNotFoundException {
		if (raceEntry != null) {

			for (Driver driver : raceEntry.getDrivers()) {
				log.debug("driver in entry: {}", driver);
				if (driver.getPlayerId() == playerResponse.getPlayerId()) {
					return driver;
				}
			}
		}
		throw new DriverNotFoundException("cannot find driver for response " + playerResponse);
	}

	public Driver getDriverByPlayerId(int playerId) {
		if (raceEntry == null) {
			return null;
		}
		log.debug("getDriverByPlayerId {} in drivers: {}", playerId, raceEntry.getDrivers());
		for (Driver driver : raceEntry.getDrivers()) {
			if (driver.getPlayerId() == playerId) {
				log.debug("driver found: {}", driver);
				return driver;
			}
		}
		return null;
	}

	/*
	public Driver getDriver(int connectionId, String name) {
		if (log.isDebugEnabled()) {
			log.debug("get driver: id=" + connectionId + " name=" + name);
			log.debug("drivers: " + drivers);
		}

		Driver driver = drivers.get(connectionId);
		if (driver != null) {
			return driver;
		}

		if (raceEntry != null) {
			for (Driver d : raceEntry.getDrivers()) {
				if (name != null && name.equals(d.getName())) {
					d.setConnectionId(connectionId);
					d.addJoin();
					drivers.put(connectionId, d);
					return d;
				}
			}
		}

		log.debug("no driver found so creating a new one");
		driver = new Driver(connectionId);
		drivers.put(connectionId, driver);
		return driver;
	}
*/

	public boolean hasRaceEntry() {
		log.debug("hasRaceEntry? {}", raceEntry);
		return raceEntry != null;
	}

	public void addRaceDriver(Driver driver) {
		log.debug("add race driver to entry: {}", driver);
		getRaceEntry().addDriver(driver);
	}

	public List<Driver> getRaceDrivers() {
		if (raceEntry == null) {
			return Collections.emptyList();
		}
		return new ArrayList<Driver>(raceEntry.getDrivers());
	}


	public void reset() {
		log.debug("---- RESET ----");
		messages = new ArrayList<String>();
		raceEntry = null;
	}

	public void addMessage(String message) {
		messages.add(message);
	}

	public boolean hasFinished(Driver driver) {
		if (hasRaceEntry()) {
			if (getRaceEntry().isQualifying()) {
				return false;
			}
			if (getRaceEntry().isPractice()) {
				return false;
			}
		}

		return driver.hasResult();
	}

	public List<Driver> getAdmins() {
		LinkedList<Driver> adminList = new LinkedList<Driver>();
		for (Driver d : getRaceEntry().getDrivers()) {
			if (d.isAdmin()) {
				adminList.add(d);
			}
		}
		return adminList;
	}

	public Driver getDriver(int connectionId) {
		if (raceEntry != null) {
			for (Driver driver : raceEntry.getDrivers()) {
				if (driver.getConnectionId() == connectionId) {
					return driver;
				}
			}
		}

		return null;
	}

}
