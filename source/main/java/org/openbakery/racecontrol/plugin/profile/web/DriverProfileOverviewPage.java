package org.openbakery.racecontrol.plugin.profile.web;

import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;
import org.openbakery.racecontrol.plugin.profile.service.DriverProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverProfileOverviewPage extends DriverProfilePage {

	private static Logger log = LoggerFactory.getLogger(DriverProfileOverviewPage.class);

	public DriverProfileOverviewPage(PageParameters parameters) {
		super(parameters);

		DriverProfileService driverProfileService = getDriverProfileService();
		if (driverProfileService == null) {
			error("Internal Error!");
			log.error("AdminMessagesService is null");
			return;
		}

		Form<DriverProfile> form = new Form<DriverProfile>("form");
		add(form);

		form.add(new AddButton(DriverProfileEditPage.class));
		try {
			List<DriverProfile> messageList = driverProfileService.getAllProfiles();

			form.add(new DriverProfileListView("adminMessages", messageList));
		} catch (PersistenceException e) {
			error("Internal error!");
			log.error(e.getMessage(), e);
			return;
		}

	}

	@Override
	public String getPageTitle() {
		return "Driver Profiles";
	}

}
