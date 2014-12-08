package org.openbakery.racecontrol.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.openbakery.jinsim.Car;
import org.openbakery.jinsim.Track;

import org.openbakery.racecontrol.plugin.tracker.data.TrackerSettings;
import org.openbakery.racecontrol.service.SettingsLoader;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

public class SettingsTest {

	@Test
	public void testSave() {
		TrackerSettings trackerSettings = new TrackerSettings();
		trackerSettings.addCar(Car.UF1);
		trackerSettings.addCar(Car.BF1);
		trackerSettings.setSignupId(1234);
		trackerSettings.setTrack(Track.BLACKWOOD_GP);

		SettingsLoader settings = new SettingsLoader(System.getProperty("java.io.tmpdir"));
		settings.save(trackerSettings);
	}

	@Test(dependsOnMethods = { "testSave" })
	public void testSettings() {
		TrackerSettings trackerSettings = new TrackerSettings();

		SettingsLoader settings = new SettingsLoader(System.getProperty("java.io.tmpdir"));
		settings.load(trackerSettings);

		assert 1234 == trackerSettings.getSignupId();
		assert Track.BLACKWOOD_GP == trackerSettings.getTrack();

		assert trackerSettings.getCars().size() == 2;
		assert trackerSettings.getCars().contains(Car.UF1);
		assert trackerSettings.getCars().contains(Car.BF1);

	}

	@Test
	public void testError() throws IOException {
		Properties properties = new Properties();
		properties.put("cars", "ASDF");
		FileOutputStream fos = new FileOutputStream(System.getProperty("java.io.tmpdir") + File.separator
				+ TrackerSettings.class.getName() + ".properties");
		properties.store(fos, "");

		TrackerSettings trackerSettings = new TrackerSettings();

		SettingsLoader settings = new SettingsLoader(System.getProperty("java.io.tmpdir"));
		settings.load(trackerSettings);

		assert 0 == trackerSettings.getCars().size();

	}

	@AfterSuite
	public void cleanup() {
		File file = new File(System.getProperty("java.io.tmpdir") + File.separator + TrackerSettings.class.getName() + ".properties");
		if (file.exists()) {
			file.delete();
		}
	}
}
