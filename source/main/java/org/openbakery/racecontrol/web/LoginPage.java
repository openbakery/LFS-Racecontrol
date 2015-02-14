package org.openbakery.racecontrol.web;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.Strings;
import org.openbakery.racecontrol.web.bean.Visibility;

public class LoginPage extends RaceControlPage {

	private String username;
	private String password;

	public LoginPage(PageParameters parameters) {
		super(parameters);


		StatelessForm form = new StatelessForm("form") {

			@Override
			protected void onSubmit() {
				if(Strings.isEmpty(username)) {
					return;
				}

				boolean authResult = AuthenticatedWebSession.get().signIn(username, password);
				if (authResult) {
					continueToOriginalDestination();
				}

			}
		};

		form.setDefaultModel(new CompoundPropertyModel(this));

		form.add(new TextField("username"));
	  form.add(new PasswordTextField("password"));

	  add(form);

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
