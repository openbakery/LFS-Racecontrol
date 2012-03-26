package org.openbakery.racecontrol.plugin.admin.messages.web;

import org.apache.wicket.markup.html.form.Button;

public class HideAllButton extends Button {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2861490813761189543L;

	public HideAllButton() {
		super("hideAll");
	}

	@Override
	public void onSubmit() {

		AdminMessagesPage adminMessagePage = (AdminMessagesPage) getPage();
		adminMessagePage.hideAllMessages();

	}

}
