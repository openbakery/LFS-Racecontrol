package org.openbakery.racecontrol.service;

import org.openbakery.racecontrol.bean.Settings;
import org.openbakery.racecontrol.plugin.tracker.data.TrackerSettings;
import org.springframework.beans.factory.annotation.Autowired;

public class SettingsService {

	private TrackerSettings trackerSettings;

	private SettingsLoader settingsLoader;

	@Autowired
	public void setSettingsLoader(SettingsLoader settingsLoader) {
		this.settingsLoader = settingsLoader;
	}

	public TrackerSettings getTrackerSettings() {
		if (trackerSettings == null) {
			trackerSettings = new TrackerSettings();
			settingsLoader.load(trackerSettings);
		}
		return trackerSettings;
	}

	public void store(Settings settings) {
		settingsLoader.save(settings);
	}

	public void storeAll() {
		store(trackerSettings);
	}

}
