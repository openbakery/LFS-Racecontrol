package org.openbakery.racecontrol.plugin.tracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openbakery.jinsim.Car;
import org.openbakery.jinsim.Track;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.persistence.ProfileHelper;
import org.openbakery.racecontrol.persistence.QueryHelper;
import org.openbakery.racecontrol.persistence.bean.Profile;
import org.openbakery.racecontrol.plugin.tracker.data.TrackerSettings;
import org.openbakery.racecontrol.service.SettingsService;
import org.openbakery.racecontrol.util.LapComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class TrackerService {

	private static Logger log = LoggerFactory.getLogger(TrackerService.class);

	private ProfileHelper profileHelper;

	private QueryHelper queryHelper;

	private SettingsService settingsService;

	@Autowired
	public void setSettingsService(SettingsService settingsService) {
		this.settingsService = settingsService;
	}

	@Autowired
	public void setProfileHelper(ProfileHelper profileHelper) {
		this.profileHelper = profileHelper;
	}

	@Autowired
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

	public List<Lap> getFastestLap() throws PersistenceException {
		TrackerSettings settings = settingsService.getTrackerSettings();

		log.debug("trackerSettings {}", settings);
		Track track = settings.getTrack();
		List<Car> cars = settings.getCars();
		int numberLaps = settings.getNumberLaps();

		return getFastestLap(track, cars, numberLaps);
	}

	public List<Lap> getFastestLap(Track track, List<Car> cars, int numberLaps) throws PersistenceException {
		log.debug("getFastestLap for track: {}, cars {}, numberLaps {}", track, cars, numberLaps);
		List<Profile> profiles = profileHelper.getSignedUpDrivers();

		ArrayList<Lap> lapList = new ArrayList<>();

		for (Profile profile : profiles) {
			Lap lap = queryHelper.getFastestLapOnServerForDriver(cars, track, profile.getLfsworldName(), numberLaps);

			if (lap == null) {
				lap = new Lap();
				Driver driver = new Driver(0);
				driver.setName(profile.getLfsworldName());
				lap.setDriver(driver);
			}
			lapList.add(lap);
		}

		Collections.sort(lapList, new LapComparator());

		int position = 1;
		for (Lap lap : lapList) {
			lap.setPosition(position++);
		}
		log.debug("lapList: {}", lapList);
		return lapList;

	}

	public List<Profile> getSignedUpDrivers() throws PersistenceException {
		return profileHelper.getSignedUpDrivers();
	}


	public Track getTrack() {
		TrackerSettings settings = settingsService.getTrackerSettings();
		return settings.getTrack();
	}
}
