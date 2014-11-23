package org.openbakery.racecontrol.plugin.profile.web;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.openbakery.racecontrol.plugin.profile.data.DriverProfile;

public class DriverProfileListView extends ListView<DriverProfile> {

	private static final long serialVersionUID = 1L;

	public DriverProfileListView(String id, List<DriverProfile> driverProfileList) {
		super(id, driverProfileList);
	}

	protected void populateItem(ListItem<DriverProfile> item) {

		DriverProfile message = item.getModelObject();

		item.add(new Label("firstname", message.getFirstname()));
		item.add(new Label("lastname", message.getLastname()));
		item.add(new Label("lfsworldName", message.getLfsworldName()));
		item.add(new Label("signedUp", message.isSignedUp()));
		item.add(new EditLink("edit", new Model<DriverProfile>(message)));
		item.add(new DeleteLink("delete", new Model<DriverProfile>(message)));
	}
}
