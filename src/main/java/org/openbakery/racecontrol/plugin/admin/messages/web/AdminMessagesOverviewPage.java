package org.openbakery.racecontrol.plugin.admin.messages.web;

import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;
import org.openbakery.racecontrol.plugin.admin.messages.service.AdminMessagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminMessagesOverviewPage extends AdminMessagesPage {

	private static Logger log = LoggerFactory.getLogger(AdminMessagesOverviewPage.class);

	public AdminMessagesOverviewPage(PageParameters pageParameters) {
		super(pageParameters);
		AdminMessagesService adminMessageService = getAdminMessageService();
		if (adminMessageService == null) {
			error("Internal Error!");
			log.error("AdminMessagesService is null");
			return;
		}

		Form<AdminMessage> form = new Form<AdminMessage>("form");
		add(form);

		form.add(new AddButton());
		form.add(new HideAllButton());
		try {
			List<AdminMessage> messageList = adminMessageService.getAllMessages();

			form.add(new AdminMessagesListView("adminMessages", messageList));
		} catch (PersistenceException e) {
			error("Internal error!");
			log.error(e.getMessage(), e);
			return;
		}
	}

	@Override
	public String getPageTitle() {
		return "Admin Messages Overview Page";
	}

}
