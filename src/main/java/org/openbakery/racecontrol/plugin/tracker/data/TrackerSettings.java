package org.openbakery.racecontrol.plugin.tracker.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sf.jinsim.Car;
import net.sf.jinsim.Track;

import org.openbakery.racecontrol.bean.Settings;

public class TrackerSettings implements Settings {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6728719580471686317L;

	public List<Car> cars;

	public Track track;

	public int signupId;

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

	public int getSignupId() {
		return signupId;
	}

	public void setSignupId(int signupId) {
		this.signupId = signupId;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public List<String> getSettingFields() {
		return Arrays.asList("cars", "track", "signupId");
	}

}
