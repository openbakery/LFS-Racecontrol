package org.openbakery.racecontrol.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.openbakery.racecontrol.bean.User;
import org.openbakery.racecontrol.plugin.tracker.web.TrackerPage;
import org.openbakery.racecontrol.service.exception.LoginFailedException;
import org.openbakery.racecontrol.web.bean.Visibility;

public class LoginPage extends RaceControlPage {

	public LoginPage(PageParameters parameters) {
		super(parameters);

		Form<User> form = new Form<User>("form");
		add(form);

		User user = getSession().getUser();

		form.add(new TextField("username", new PropertyModel(user, "username")));
		form.add(new PasswordTextField("password", new PropertyModel(user, "password")));

		form.add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(LoginPage.class);
			}
		});
		form.add(new Button("login") {
			@Override
			public void onSubmit() {
				User user = ((RaceControlSession) getSession()).getUser();
				try {
					((RaceControlSession) getSession()).getServiceLocator().getLoginService().login(user);
					setResponsePage(TrackerPage.class);
				} catch (LoginFailedException e) {
					error("Unknown Username/Password");
					setResponsePage(LoginPage.class);
				}
			}
		});

	}

	@Override
	public String getPageTitle() {
		return "Login";
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.NOT_AUTHENTICATED;
	}
}
