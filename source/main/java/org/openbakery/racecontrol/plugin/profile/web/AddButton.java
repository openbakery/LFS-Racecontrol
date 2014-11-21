package org.openbakery.racecontrol.plugin.profile.web;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;

public class AddButton extends Button {

	private static final long serialVersionUID = 1L;

	private Class<? extends WebPage> webPageClass;

	public AddButton(Class<? extends WebPage> webPageClass) {
		super("add");
		this.webPageClass = webPageClass;
	}

	@Override
	public void onSubmit() {
		setResponsePage(webPageClass);
	}
}
