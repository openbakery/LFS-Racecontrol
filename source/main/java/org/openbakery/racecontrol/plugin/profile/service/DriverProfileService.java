package org.openbakery.racecontrol.plugin.profile.service;

import java.util.List;

import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;
import org.openbakery.racecontrol.plugin.profile.data.TeamProfile;
import org.springframework.beans.factory.annotation.Autowired;

public class DriverProfileService {

	@Autowired
	private Persistence persistence;

	public DriverProfileService() {
	}

	public void setPersistence(Persistence persistence) {
		this.persistence = persistence;
	}

	public void store(DriverProfile profile) throws PersistenceException {
		persistence.store(profile);
	}

	public void store(TeamProfile profile) throws PersistenceException {
		persistence.store(profile);
	}

	public void delete(DriverProfile profile) throws PersistenceException {
		persistence.delete(profile);
	}

	public void delete(TeamProfile profile) throws PersistenceException {
		persistence.delete(profile);
	}

	@SuppressWarnings("unchecked")
	public List<DriverProfile> getAllProfiles() throws PersistenceException {
		return (List<DriverProfile>) persistence.query("Select driverProfile from DriverProfile as driverProfile");
	}
}
