package org.openbakery.racecontrol.plugin.tracker.web;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.openbakery.jinsim.Car;
import org.openbakery.jinsim.Track;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.string.StringValueConversionException;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.persistence.bean.Profile;
import org.openbakery.racecontrol.plugin.tracker.TrackerService;
import org.openbakery.racecontrol.plugin.tracker.data.TrackerSettings;
import org.openbakery.racecontrol.service.ServiceLocateException;
import org.openbakery.racecontrol.service.ServiceLocator;
import org.openbakery.racecontrol.web.RaceControlPage;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackerPage extends RaceControlPage {

	private static Logger log = LoggerFactory.getLogger(TrackerPage.class);

	private Track track = null;

	private List<Car> cars = null;

	private int numberLaps;

	public TrackerPage(PageParameters parameters) throws PersistenceException {
		super(parameters);

		TrackerSettings settings = getSession().getServiceLocator().getSettingsService().getTrackerSettings();
		log.debug("trackerSettings {}", settings);


    Set<String> keys = parameters.getNamedKeys();
		if (keys.contains("track") && keys.contains("car") && keys.contains("numberLaps")) {
			try {

				track = Track.getTrackByShortName(parameters.get("track").toString());
				String carString = parameters.get("car").toString();
				cars = new ArrayList<Car>();
				for (String carName : carString.split(",")) {
					cars.add(Car.getCarByName(carName));
				}
				try {
					numberLaps = parameters.get("numberLaps").toInt();
				} catch (StringValueConversionException ex) {
					error("given numberLaps is not a number");
				}
			} catch (IllegalArgumentException ex) {
				// error handling is below
			}
		} else {
			track = settings.getTrack();
			cars = settings.getCars();
			numberLaps = settings.getNumberLaps();
		}

		String description;
		List<Lap> lapList = null;

		if (track != null && cars != null) {
			lapList = getTrackerService().getFastestLap(track, cars, numberLaps);
			StringBuilder carString = new StringBuilder();
			boolean first = true;
			for (Car car : cars) {
				if (!first) {
					carString.append(", ");
				} else {
					first = false;
				}
				carString.append(car.getLongname());
			}

			description = MessageFormat.format(getString("times_for"), track.getName(), carString);

		} else {
			lapList = Collections.emptyList();
			description = "";
		}

		add(new Label("description", description));
		List<Profile> profiles = getTrackerService().getSignedUpDrivers();
		log.debug("display laps for: {}", profiles);
		add(new LapListView("laps", lapList, profiles, track));
	}

	@Override
	public String getPageTitle() {
		return "Tracker";
	}

	public TrackerService getTrackerService() {
		ServiceLocator serviceLocator = getSession().getServiceLocator();
		try {
			return (TrackerService) serviceLocator.getService(TrackerService.class);
		} catch (ServiceLocateException e) {
			error("Internal error!");
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.ALWAYS;
	}

}
