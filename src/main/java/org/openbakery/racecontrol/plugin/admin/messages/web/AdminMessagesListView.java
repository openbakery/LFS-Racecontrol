package org.openbakery.racecontrol.plugin.admin.messages.web;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;

public class AdminMessagesListView extends ListView<AdminMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1780436404733923405L;

	public AdminMessagesListView(String id, List<AdminMessage> adminMessageList) {
		super(id, adminMessageList);
	}

	protected void populateItem(ListItem<AdminMessage> item) {

		AdminMessage message = item.getModelObject();

		item.add(new Label("key", message.getKey()));
		item.add(new Label("message", message.getMessage()));
		item.add(new Label("duration", Integer.toString(message.getDuration())));
		item.add(new Label("countdown", Boolean.toString(message.getCountdown())));
		item.add(new EditLink("edit", new Model<AdminMessage>(message)));
		item.add(new DeleteLink("delete", new Model<AdminMessage>(message)));
		item.add(new SendLink("send", new Model<AdminMessage>(message)));
	}
}
