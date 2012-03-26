package org.openbakery.racecontrol.plugin.result;

import java.util.Collections;
import java.util.List;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ResultDataAccess {

	@Autowired
	@Qualifier("databasePersistence")
	private Persistence persistence;

	public List<Driver> getResult(int raceEntryId) throws PersistenceException {

		List<Driver> drivers = (List<Driver>) persistence.query("select d from Driver d where d.id = " + raceEntryId);

		return Collections.emptyList();
	}

}
