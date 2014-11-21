package org.openbakery.racecontrol.plugin.admin.messages.web;

import org.apache.wicket.markup.html.form.Button;

public class AddButton extends Button {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2861490813761189543L;

	public AddButton() {
		super("add");
	}

	@Override
	public void onSubmit() {
		setResponsePage(AdminMessagesEditPage.class);
	}
}
