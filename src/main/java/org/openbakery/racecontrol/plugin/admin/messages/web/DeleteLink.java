package org.openbakery.racecontrol.plugin.admin.messages.web;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;

public class DeleteLink extends Link<AdminMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4362358310047172583L;

	public DeleteLink(String id, IModel<AdminMessage> object) {
		super(id, object);
	}

	@Override
	public void onClick() {
		AdminMessagesPage adminMessagePage = (AdminMessagesPage) getPage();
		adminMessagePage.delete(getModelObject());
		setResponsePage(AdminMessagesOverviewPage.class);
	}

}
