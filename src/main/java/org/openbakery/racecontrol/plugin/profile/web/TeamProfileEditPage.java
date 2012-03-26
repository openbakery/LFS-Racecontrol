package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.openbakery.racecontrol.plugin.profile.data.TeamProfile;
import org.openbakery.racecontrol.web.page.common.RedirectLink;

public class TeamProfileEditPage extends DriverProfilePage {

	public TeamProfileEditPage(PageParameters parameters) {
		this(parameters, new TeamProfile());
	}

	public TeamProfileEditPage(PageParameters pageParameters, TeamProfile teamProfile) {
		super(pageParameters);
		Form<TeamProfile> form = new Form<TeamProfile>("form");
		add(form);

		form.add(new TextField<TeamProfile>("name", new PropertyModel<TeamProfile>(teamProfile, "name")));

		form.add(new RedirectLink("cancel", TeamProfileOverviewPage.class));

		form.add(new TeamProfileSaveButton("save", teamProfile, this));

	}

	@Override
	public String getPageTitle() {
		return "Team Profile Edit Page";
	}

}
