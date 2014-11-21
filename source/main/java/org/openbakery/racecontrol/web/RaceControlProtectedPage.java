package org.openbakery.racecontrol.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class RaceControlProtectedPage extends RaceControlPage {

	public RaceControlProtectedPage(PageParameters parameters) {
		super(parameters);
		if (!getSession().isLoggedIn()) {
			setResponsePage(LoginPage.class);
		}
	}
}
