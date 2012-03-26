package org.openbakery.racecontrol.plugin.admin.messages.web;

import org.apache.wicket.markup.html.form.Button;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;

public class AdminMessagesSaveButton extends Button {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2829358608908372553L;

	private AdminMessage message;

	private AdminMessagesEditPage adminMessagesAddPage;

	public AdminMessagesSaveButton(String string, AdminMessage message, AdminMessagesEditPage adminMessagesAddPage) {
		super(string);
		this.message = message;
		this.adminMessagesAddPage = adminMessagesAddPage;
	}

	@Override
	public void onSubmit() {
		adminMessagesAddPage.store(message);
		setResponsePage(AdminMessagesOverviewPage.class);
	}

}
