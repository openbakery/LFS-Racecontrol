package org.openbakery.racecontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sf.jinsim.response.PlayerResponse;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.RaceEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Race {

  private static Logger log = LoggerFactory.getLogger(Race.class);

    private RaceEntry raceEntry;

	private ArrayList<Driver> drivers;

	private ArrayList<String> messages;

	public Race() {
		drivers = new ArrayList<Driver>(48);
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


	public ArrayList<Driver> getDrivers() {
		return drivers;
	}

	public Driver getRaceDriver(PlayerResponse playerResponse) throws DriverNotFoundException {
		if (raceEntry != null) {
			for (Driver driver : raceEntry.getDrivers()) {
				if (driver.getPlayerId() == playerResponse.getPlayerId()) {
					return driver;
				}
			}
		}
		throw new DriverNotFoundException("cannot find driver for response " + playerResponse);
	}

	public Driver getDriverByPlayerId(int playerId) {
		for (Driver driver : drivers) {
			if (driver.getPlayerId() == playerId) {
				return driver;
			}
		}
		return null;
	}

	public Driver getDriver(int connectionId, String name) {
		if (log.isDebugEnabled()) {
			log.debug("get driver: id=" + connectionId + " name=" + name);
			log.debug("drivers: " + drivers);
		}

		for (Driver driver : drivers) {
			if (driver.getConnectionId() == connectionId) {
				return driver;
			}
		}

		if (raceEntry != null) {
			for (Driver d : raceEntry.getDrivers()) {
				if (name != null && name.equals(d.getName())) {
					d.setConnectionId(connectionId);
					d.addJoin();
					drivers.add(d);
					return d;
				}
			}
		}

		log.debug("no driver found so creating a new one");
		Driver driver = new Driver(connectionId);
		drivers.add(driver);
		return driver;
	}

	public boolean hasRaceEntry() {
		return raceEntry != null;
	}

	public void addRaceDriver(Driver driver) {
		getRaceEntry().addDriver(driver);
	}

	public List<Driver> getRaceDrivers() {
		if (raceEntry == null) {
			return Collections.emptyList();
		}

		ArrayList<Driver> raceDriverList = new ArrayList<Driver>(raceEntry.getDrivers().size());

		raceDriverList.addAll(raceEntry.getDrivers());
		return raceDriverList;
	}

	public void setDriverInactive(Driver driver) {

		for (Driver d : drivers) {
			if (d.getName().equals(driver.getName())) {
				drivers.remove(d);
				break;
			}
		}

		if (raceEntry != null) {
			for (Driver d : raceEntry.getDrivers()) {
				if (d.getName().equals(driver.getName())) {
					d.setConnectionId(0);
					d.setPlayerId(0);
				}
			}
		}

	}

	public void reset() {
		messages = new ArrayList<String>();
		raceEntry = null;
	}

	public void addMessage(String message) {
		messages.add(message);
	}

	public boolean hasFinished(Driver driver) {
		if (hasRaceEntry() && getRaceEntry().isQualifying()) {
			return false;
		}
		return driver.hasResult();
	}

	public List<Driver> getAdmins() {
		LinkedList<Driver> adminList = new LinkedList<Driver>();
		for (Driver d : drivers) {
			if (d.isAdmin()) {
				adminList.add(d);
			}
		}
		return adminList;
	}

	public Driver getDriver(int connectionId) {
		return getDriver(connectionId, "");
	}

}
