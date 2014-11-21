package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.markup.html.form.Button;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;

public class DriverProfileSaveButton extends Button {

	private static final long serialVersionUID = 1L;

	private DriverProfile message;

	private DriverProfileEditPage driverProfileAddPage;

	public DriverProfileSaveButton(String string, DriverProfile message, DriverProfileEditPage driverProfileAddPage) {
		super(string);
		this.message = message;
		this.driverProfileAddPage = driverProfileAddPage;
	}

	@Override
	public void onSubmit() {
		driverProfileAddPage.store(message);
		setResponsePage(DriverProfileOverviewPage.class);
	}

}
