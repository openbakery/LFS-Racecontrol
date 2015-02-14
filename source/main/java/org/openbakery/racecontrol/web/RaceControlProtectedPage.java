package org.openbakery.racecontrol.web;

import org.apache.wicket.Application;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class RaceControlProtectedPage extends RaceControlPage {

	public RaceControlProtectedPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		RaceControlWebApplication application = (RaceControlWebApplication) Application.get();
		//if user is not signed in, redirect him to sign in page
		if (!AuthenticatedWebSession.get().isSignedIn()) {
			application.restartResponseAtSignInPage();
		}
	}
}
