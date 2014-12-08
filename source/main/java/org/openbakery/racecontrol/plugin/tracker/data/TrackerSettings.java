package org.openbakery.racecontrol.plugin.tracker.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openbakery.jinsim.Car;
import org.openbakery.jinsim.Track;

import org.openbakery.racecontrol.bean.Settings;

public class TrackerSettings implements Settings {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6728719580471686317L;

	public List<Car> cars;

	public Track track;

	private int numberLaps;

	public TrackerSettings() {
		cars = new LinkedList<Car>();
	}

	public List<Car> getCars() {
		if (cars == null) {
			return Collections.emptyList();
		}
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	public void addCar(Car car) {
		cars.add(car);
	}

	public int getNumberLaps() {
		return numberLaps;
	}

	public void setNumberLaps(int numberLaps) {
		this.numberLaps = numberLaps;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public List<String> getSettingFields() {
		return Arrays.asList("cars", "track", "numberLaps");
	}


	@Override
	public String toString() {
		return "TrackerSettings{" +
						"cars=" + cars +
						", track=" + track +
						", numberLaps=" + numberLaps +
						'}';
	}
}
