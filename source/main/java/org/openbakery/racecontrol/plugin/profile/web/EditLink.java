package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;

public class EditLink extends Link<DriverProfile> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1863158126866650367L;

	public EditLink(String id, IModel<DriverProfile> object) {
		super(id, object);
	}

	@Override
	public void onClick() {
		setResponsePage(new DriverProfileEditPage(new PageParameters(), getModelObject()));
	}
}
