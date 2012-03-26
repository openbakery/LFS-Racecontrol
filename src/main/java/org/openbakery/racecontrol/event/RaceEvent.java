package org.openbakery.racecontrol.event;

import org.openbakery.racecontrol.Race;
import org.openbakery.racecontrol.data.Driver;

public class RaceEvent {
	
	public enum Type {
		STARTED,
		END,
		NEW_DRIVER;
	}
	
	private Type type;
	
	private Race race;
	
	private Driver driver;
	
	public RaceEvent(Type type, Race race, Driver driver) {
		this.race = race;
		this.type = type;
		this.driver = driver;
	}

	public Race getRace() {
		return race;
	}

	public Type getType() {
		return type;
	}

	public Driver getDriver() {
		return driver;
	}

}
