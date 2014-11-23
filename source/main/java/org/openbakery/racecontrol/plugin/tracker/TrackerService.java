package org.openbakery.racecontrol.plugin.tracker;

import java.util.List;

import net.sf.jinsim.Car;
import net.sf.jinsim.Track;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.persistence.ProfileHelper;
import org.openbakery.racecontrol.persistence.QueryHelper;
import org.openbakery.racecontrol.persistence.bean.Profile;
import org.openbakery.racecontrol.plugin.tracker.data.TrackerSettings;
import org.openbakery.racecontrol.service.SettingsService;
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

		Track track = settings.getTrack();
		List<Car> cars = settings.getCars();

		return getFastestLap(track, cars);
	}

	public List<Lap> getFastestLap(Track track, List<Car> cars) throws PersistenceException {
		log.debug("getFastestLap");
		List<Profile> profiles = profileHelper.getSignedUpDrivers();
		List<Lap> lapList = queryHelper.getFastestsLaps(cars, track, profiles);

		// add drivers that has not driven a lap yet
		for (Profile profile : profiles) {
			String name = profile.getLfsworldName();
			boolean found = false;
			for (Lap lap : lapList) {
				if (lap.getDriver().getName().equalsIgnoreCase(name)) {
					found = true;
				}
			}

			if (!found) {
				log.debug("no lap found for driver {} so add empty", profile.getLfsworldName());
				Lap newLap = new Lap();
				Driver newDriver = new Driver();
				newDriver.setName(profile.getLfsworldName());
				newLap.setDriver(newDriver);
				lapList.add(newLap);
			}

		}
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
