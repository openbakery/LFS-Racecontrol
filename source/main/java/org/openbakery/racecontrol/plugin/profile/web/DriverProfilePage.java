package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;
import org.openbakery.racecontrol.plugin.profile.data.TeamProfile;
import org.openbakery.racecontrol.plugin.profile.service.DriverProfileService;
import org.openbakery.racecontrol.service.ServiceLocateException;
import org.openbakery.racecontrol.service.ServiceLocator;
import org.openbakery.racecontrol.web.RaceControlPage;
import org.openbakery.racecontrol.web.RaceControlProtectedPage;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DriverProfilePage extends RaceControlProtectedPage {

	private static Logger log = LoggerFactory.getLogger(DriverProfilePage.class);

	public DriverProfilePage(PageParameters parameters) {
		super(parameters);
	}

	public DriverProfileService getDriverProfileService() {
		ServiceLocator serviceLocator = getSession().getServiceLocator();
		try {
			return (DriverProfileService) serviceLocator.getService(DriverProfileService.class);
		} catch (ServiceLocateException e) {
			error("Internal error!");
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public void store(DriverProfile profile) {
		try {
			getDriverProfileService().store(profile);
		} catch (PersistenceException e) {
			error("Unable to store the driver profile");
			log.error(e.getMessage(), e);
		}
	}

	public void store(TeamProfile teamProfile) {
		try {
			getDriverProfileService().store(teamProfile);
		} catch (PersistenceException e) {
			error("Unable to store the team profile");
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.AUTHENTICATED;
	}

	public void delete(DriverProfile profile) {
		try {
			getDriverProfileService().delete(profile);
		} catch (PersistenceException e) {
			error("Unable to store the driver profile");
			log.error(e.getMessage(), e);
		}
	}

	public void delete(TeamProfile profile) {
		try {
			getDriverProfileService().delete(profile);
		} catch (PersistenceException e) {
			error("Unable to store the driver profile");
			log.error(e.getMessage(), e);
		}
	}

}
