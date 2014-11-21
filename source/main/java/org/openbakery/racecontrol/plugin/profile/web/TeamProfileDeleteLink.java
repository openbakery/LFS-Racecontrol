package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.openbakery.racecontrol.plugin.profile.data.TeamProfile;

public class TeamProfileDeleteLink extends Link<TeamProfile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TeamProfileDeleteLink(String id, IModel<TeamProfile> model) {
		super(id, model);
	}

	@Override
	public void onClick() {
		DriverProfilePage driverProfilePage = (DriverProfilePage) getPage();
		driverProfilePage.delete(getModelObject());
		setResponsePage(TeamProfileOverviewPage.class);

	}

}
