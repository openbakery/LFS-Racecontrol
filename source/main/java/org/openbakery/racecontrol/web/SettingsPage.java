package org.openbakery.racecontrol.web;

import java.io.IOException;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.openbakery.racecontrol.JInSimClient;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.web.bean.InSimSettings;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SettingsPage extends RaceControlProtectedPage {

	private Logger log = LoggerFactory.getLogger(SettingsPage.class);

	private transient JInSimClient client;

	public SettingsPage(PageParameters parameters) {
		super(parameters);
		Form<InSimSettings> form = new Form<InSimSettings>("form");
		add(form);

		final InSimSettings settings = getClient().getSettings();

		String message;
		if (getClient().isConnected()) {
			message = "Client is connected";
		} else {
			message = "No client connection";
		}
		add(new Label("serverStatus", message));

		form.add(new TextField("name", new PropertyModel(settings, "name")));
		form.add(new TextField("hostname", new PropertyModel(settings, "hostname")));
		form.add(new TextField("port", new PropertyModel(settings, "port")));
		form.add(new PasswordTextField("adminPassword", new PropertyModel(settings, "adminPassword")).setRequired(false));

		form.add(new Link("stop") {
			@Override
			public void onClick() {
				try {
					getRaceControl().stop();
				} catch (IOException e) {
					error("Cannot disconnect to the host!");
					log.error("Cannot disconnect to the host!", e);
				}
				setResponsePage(SettingsPage.class);
			}
		});

		form.add(new Button("start") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3800975831949933746L;

			@Override
			public void onSubmit() {
				try {
					log.debug("settings: {}", getClient().getSettings());
					getRaceControl().start();
				} catch (IOException e) {
					error("Cannot connect to the host!");
					log.error("Cannot connect to the host!", e);
				}
				setResponsePage(SettingsPage.class);

			}
		});

	}

	@Override
	public String getPageTitle() {
		return "Settings";
	}

	private JInSimClient getClient() {
		return getSession().getServiceLocator().getRaceService().getClient();
	}

	private RaceControl getRaceControl() {
		return getSession().getServiceLocator().getRaceService().getRaceControl();
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.AUTHENTICATED;
	}

}
