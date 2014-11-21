package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.openbakery.racecontrol.plugin.profile.data.TeamProfile;

public class TeamEditLink extends Link<TeamProfile> {

	private static final long serialVersionUID = 1L;

	public TeamEditLink(String id, IModel<TeamProfile> object) {
		super(id, object);
	}

	@Override
	public void onClick() {
		setResponsePage(new TeamProfileEditPage(new PageParameters(), getModelObject()));
	}

}
