package org.openbakery.racecontrol.service;

import org.openbakery.racecontrol.persistence.ProfileHelper;
import org.openbakery.racecontrol.persistence.QueryHelper;

public class DatabaseService {

	private ProfileHelper profileHelper;

	private QueryHelper queryHelper;

	public ProfileHelper getProfileHelper() {
		return profileHelper;
	}

	public void setProfileHelper(ProfileHelper profileHelper) {
		this.profileHelper = profileHelper;
	}

	public QueryHelper getQueryHelper() {
		return queryHelper;
	}

	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}

}
