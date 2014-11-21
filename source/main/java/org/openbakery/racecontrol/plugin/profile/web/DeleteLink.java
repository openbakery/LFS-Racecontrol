package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;

public class DeleteLink extends Link<DriverProfile> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeleteLink(String id, IModel<DriverProfile> object) {
		super(id, object);
	}

	@Override
	public void onClick() {
		DriverProfilePage driverProfilePage = (DriverProfilePage) getPage();
		driverProfilePage.delete(getModelObject());
		setResponsePage(DriverProfileOverviewPage.class);
	}

}
