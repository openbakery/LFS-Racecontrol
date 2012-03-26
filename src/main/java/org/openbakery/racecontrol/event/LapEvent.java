package org.openbakery.racecontrol.event;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;

public class LapEvent {
	
	private Driver driver;
	private int split;
	private Lap lap;



	public LapEvent(Driver driver, Lap lap) {
		this(driver, lap, 0);
	}
	
	public LapEvent(Driver driver, Lap lap, int split) {
		this.driver = driver;
		this.split = split;
		this.lap = lap;
	}

	public Driver getDriver() {
		return driver;
	}
	
	public Lap getLap() {
		return lap;
	}

	public int getSplit() {
		return split;
	}
	
	

}
