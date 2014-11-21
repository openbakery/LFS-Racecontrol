package org.openbakery.racecontrol.plugin.admin.messages.web;

import java.util.ArrayList;
import java.util.EnumSet;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.openbakery.racecontrol.gui.MessageSize;
import org.openbakery.racecontrol.plugin.admin.messages.data.AdminMessage;
import org.openbakery.racecontrol.web.page.common.RedirectLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminMessagesEditPage extends AdminMessagesPage {

	private static Logger log = LoggerFactory.getLogger(AdminMessagesEditPage.class);

	public AdminMessagesEditPage(PageParameters pageParameters) {
		this(pageParameters, new AdminMessage());
	}

	public AdminMessagesEditPage(PageParameters pageParameters, AdminMessage message) {
		super(pageParameters);
		Form<AdminMessage> form = new Form<AdminMessage>("form");
		add(form);

		form.add(new TextField("key", new PropertyModel(message, "key")));
		form.add(new TextField("message", new PropertyModel(message, "message")));
		form.add(new TextField("duration", new PropertyModel(message, "duration")).setRequired(false));
		form.add(new CheckBox("countdown", new PropertyModel(message, "countdown")).setRequired(false));
		form.add(new ListChoice("size", new PropertyModel(message, "size"), new ArrayList<MessageSize>(EnumSet.allOf(MessageSize.class))));

		form.add(new RedirectLink("cancel", AdminMessagesOverviewPage.class));

		form.add(new AdminMessagesSaveButton("save", message, this));

	}

	@Override
	public String getPageTitle() {
		return "Admin Message Add Page";
	}

}
