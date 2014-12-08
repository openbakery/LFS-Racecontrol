package org.openbakery.racecontrol.plugin.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openbakery.jinsim.response.InSimResponse;

import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.persistence.ProfileHelper;
import org.openbakery.racecontrol.persistence.bean.Profile;
import org.openbakery.racecontrol.plugin.Plugin;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;
import org.openbakery.racecontrol.plugin.profile.service.DriverProfileService;
import org.openbakery.racecontrol.plugin.profile.web.DriverProfileOverviewPage;
import org.openbakery.racecontrol.plugin.profile.web.TeamProfileOverviewPage;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.springframework.beans.factory.annotation.Autowired;

public class DriverProfilePlugin implements Plugin, ProfileHelper {

	@Autowired
	private DriverProfileService driverProfileService;

	public String getHelp() {
		return "Plugin to manage driver profiles";
	}

	public List<MenuItem> getMenuItems() {
		return Arrays.asList(new MenuItem("Driver", DriverProfileOverviewPage.class, Visibility.AUTHENTICATED, 0), new MenuItem("Team", TeamProfileOverviewPage.class, Visibility.AUTHENTICATED, 1));
	}

	public String getName() {
		return "DriverProfile";
	}

	public void packetReceived(InSimResponse response) {
	}

	public List<Profile> getSignedUpDrivers() throws PersistenceException {

		List<DriverProfile> driverProfileList = driverProfileService.getAllProfiles();

		List<Profile> profileList = new ArrayList<Profile>(driverProfileList.size());

		for (DriverProfile driverProfile : driverProfileList) {
			profileList.add(driverProfile.getProfile());
		}
		return profileList;

	}

}
