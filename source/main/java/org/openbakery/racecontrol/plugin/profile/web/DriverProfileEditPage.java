package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.PropertyModel;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;
import org.openbakery.racecontrol.web.page.common.RedirectLink;

public class DriverProfileEditPage extends DriverProfilePage {

	public DriverProfileEditPage(PageParameters parameters) {
		this(parameters, new DriverProfile());
	}

	public DriverProfileEditPage(PageParameters pageParameters, DriverProfile driverProfile) {
		super(pageParameters);
		Form<DriverProfile> form = new Form<DriverProfile>("form");
		add(form);

		form.add(new TextField<DriverProfile>("firstname", new PropertyModel<DriverProfile>(driverProfile, "firstname")));
		form.add(new TextField<DriverProfile>("lastname", new PropertyModel<DriverProfile>(driverProfile, "lastname")));
		form.add(new TextField<DriverProfile>("lfsworldName", new PropertyModel<DriverProfile>(driverProfile, "lfsworldName")));
		form.add(new CheckBox("signedUp", new PropertyModel(driverProfile, "signedUp")));

		form.add(new RedirectLink("cancel", DriverProfileOverviewPage.class));

		form.add(new DriverProfileSaveButton("save", driverProfile, this));

	}

	@Override
	public String getPageTitle() {
		return "Driver Profile Edit Page";
	}

}
