package org.openbakery.racecontrol.plugin.restriction.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.openbakery.racecontrol.plugin.restriction.data.Restrictions;
import org.openbakery.racecontrol.plugin.restriction.service.RestrictionsService;
import org.openbakery.racecontrol.service.ServiceLocateException;
import org.openbakery.racecontrol.service.ServiceLocator;
import org.openbakery.racecontrol.web.RaceControlPage;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestictionsPage extends RaceControlPage {

	private static Logger logger = LoggerFactory.getLogger(RaceControlPage.class);

	public RestictionsPage(PageParameters parameters) {
		super(parameters);

		Form<Restrictions> form = new Form<Restrictions>("form");
		add(form);

		RestrictionsService service = getSession().getService(RestrictionsService.class);

		Restrictions restrictions = new Restrictions(service.getRestrictions());

		form.add(new TextField("intake", new PropertyModel(restrictions, "intake")));
		form.add(new TextField("mass", new PropertyModel(restrictions, "mass")));

		form.add(new SaveButton("Save", restrictions));

	}

	@Override
	public String getPageTitle() {
		return "Restrictions";
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.AUTHENTICATED;
	}

	public RestrictionsService getRestrictionsService() {
		ServiceLocator serviceLocator = getSession().getServiceLocator();
		try {
			return (RestrictionsService) serviceLocator.getService(RestrictionsService.class);
		} catch (ServiceLocateException e) {
			error("Internal error!");
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
