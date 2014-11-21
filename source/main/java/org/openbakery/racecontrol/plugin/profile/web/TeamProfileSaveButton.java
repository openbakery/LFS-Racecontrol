package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.markup.html.form.Button;
import org.openbakery.racecontrol.plugin.profile.data.TeamProfile;

public class TeamProfileSaveButton extends Button {

	private TeamProfile teamProfile;

	private TeamProfileEditPage teamProfileAddPage;

	public TeamProfileSaveButton(String string, TeamProfile teamProfile, TeamProfileEditPage teamProfileAddPage) {
		super(string);
		this.teamProfile = teamProfile;
		this.teamProfileAddPage = teamProfileAddPage;
	}

	@Override
	public void onSubmit() {
		teamProfileAddPage.store(teamProfile);
		setResponsePage(TeamProfileOverviewPage.class);
	}

}
