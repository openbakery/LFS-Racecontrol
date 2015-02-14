package org.openbakery.racecontrol.web;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.openbakery.racecontrol.web.bean.Visibility;

public class LogoutPage extends RaceControlPage {

	public LogoutPage(PageParameters parameters) {
		super(parameters);
		AuthenticatedWebSession.get().invalidate();
		setResponsePage(LoginPage.class);
	}

	@Override
	public String getPageTitle() {
		return "Logout";
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.AUTHENTICATED;
	}
}
