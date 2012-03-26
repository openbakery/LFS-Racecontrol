package org.openbakery.racecontrol.plugin.penalty.web;

import java.util.List;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.plugin.penalty.data.Penalty;
import org.openbakery.racecontrol.plugin.penalty.service.PenaltyService;
import org.openbakery.racecontrol.service.ServiceLocateException;
import org.openbakery.racecontrol.service.ServiceLocator;
import org.openbakery.racecontrol.web.RaceControlPage;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PenaltyPage extends RaceControlPage {

	public PenaltyPage(PageParameters parameters) {
		super(parameters);

		Form<Driver> form = new Form<Driver>("form");
		add(form);

		List<Driver> driverList = getPenaltyServiceService().getDrivers();
		form.add(new PenaltyDriverListView("driverList", driverList));

	}

	private static Logger log = LoggerFactory.getLogger(PenaltyPage.class);

	public PenaltyService getPenaltyServiceService() {
		ServiceLocator serviceLocator = getSession().getServiceLocator();
		try {
			return (PenaltyService) serviceLocator.getService(PenaltyService.class);
		} catch (ServiceLocateException e) {
			error("Internal error!");
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String getPageTitle() {
		return "Penalty";
	}

	@Override
	public Visibility getVisibility() {
		return Visibility.AUTHENTICATED;
	}

	public void sendPenalty(Driver driver, Penalty.Type type) {
		getPenaltyServiceService().sendPenalty(driver, type);
	}

}
