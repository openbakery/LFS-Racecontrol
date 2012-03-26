package org.openbakery.racecontrol.plugin.admin.messages.web;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;

public class SendLink extends Link<AdminMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1863158126866650367L;

	public SendLink(String id, IModel<AdminMessage> object) {
		super(id, object);
	}

	@Override
	public void onClick() {
		info("Sending message to all users: " + getModelObject().getMessage());
		AdminMessagesPage adminMessagePage = (AdminMessagesPage) getPage();
		adminMessagePage.sendToAllUsers(getModelObject());
	}
}
