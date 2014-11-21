package org.openbakery.racecontrol.plugin.restriction.service;

import org.openbakery.racecontrol.plugin.restriction.data.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestrictionsService {

	private static Logger logger = LoggerFactory.getLogger(RestrictionsService.class);

	private Restrictions restrictions;

	public RestrictionsService() {
		restrictions = new Restrictions();
	}

	public Restrictions getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(Restrictions restrictions) {
		this.restrictions = restrictions;
	}

}
