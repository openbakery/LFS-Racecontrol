package org.openbakery.racecontrol.plugin.profile.web;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.openbakery.racecontrol.plugin.profile.data.TeamProfile;

public class TeamProfileListView extends ListView<TeamProfile> {
	private static final long serialVersionUID = 1L;

	public TeamProfileListView(String id, List<TeamProfile> teamProfileList) {
		super(id, teamProfileList);
	}

	protected void populateItem(ListItem<TeamProfile> item) {

		TeamProfile profile = item.getModelObject();

		item.add(new Label("name", profile.getName()));
		item.add(new TeamEditLink("edit", new Model<TeamProfile>(profile)));
		item.add(new TeamProfileDeleteLink("delete", new Model<TeamProfile>(profile)));
	}
}
