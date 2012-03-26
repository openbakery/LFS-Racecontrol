package org.openbakery.racecontrol.plugin.penalty.web;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.plugin.penalty.data.Penalty;

public class PenaltyDriverListView extends ListView<Driver> {

	private static final long serialVersionUID = 1L;

	public PenaltyDriverListView(String id, List<Driver> driverList) {
		super(id, driverList);
	}

	@Override
	protected void populateItem(ListItem<Driver> item) {

		Driver driver = item.getModelObject();
		item.add(new Label("name", driver.getName()));
		item.add(new PenaltyLink("driveThrough", new Model<Driver>(driver), Penalty.Type.DRIVE_THOUGH));
		item.add(new PenaltyLink("stopAndGo", new Model<Driver>(driver), Penalty.Type.STOP_AND_GO));
		item.add(new PenaltyLink("clear", new Model<Driver>(driver), Penalty.Type.CLEAR));

	}

}
