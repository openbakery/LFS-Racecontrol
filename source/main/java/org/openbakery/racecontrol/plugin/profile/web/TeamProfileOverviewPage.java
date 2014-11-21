package org.openbakery.racecontrol.plugin.profile.web;

import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;
import org.openbakery.racecontrol.plugin.profile.data.TeamProfile;
import org.openbakery.racecontrol.plugin.profile.service.DriverProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamProfileOverviewPage extends DriverProfilePage {

	private static Logger log = LoggerFactory.getLogger(DriverProfileOverviewPage.class);

	public TeamProfileOverviewPage(PageParameters parameters) {

		super(parameters);

		DriverProfileService driverProfileService = getDriverProfileService();
		if (driverProfileService == null) {
			error("Internal Error!");
			log.error("DriverProfileService is null");
			return;
		}

		Form<TeamProfile> form = new Form<TeamProfile>("form");
		add(form);

		form.add(new AddButton(TeamProfileEditPage.class));
		try {
			List<DriverProfile> messageList = driverProfileService.getAllProfiles();

			form.add(new DriverProfileListView("teamProfileList", messageList));
		} catch (PersistenceException e) {
			error("Internal error!");
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public String getPageTitle() {
		return "Team Profile Overview Page";
	}

}
