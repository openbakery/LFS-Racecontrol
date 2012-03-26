package org.openbakery.racecontrol.plugin.admin.messages.web;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;

public class EditLink extends Link<AdminMessage> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1863158126866650367L;

	public EditLink(String id, IModel<AdminMessage> object) {
		super(id, object);
	}

	@Override
	public void onClick() {
		setResponsePage(new AdminMessagesEditPage(new PageParameters(), getModelObject()));
	}
}
