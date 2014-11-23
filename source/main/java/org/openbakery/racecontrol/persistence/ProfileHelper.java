package org.openbakery.racecontrol.persistence;

import java.util.List;

import org.openbakery.racecontrol.persistence.bean.Profile;

public interface ProfileHelper {

	public List<Profile> getSignedUpDrivers() throws PersistenceException;

}
