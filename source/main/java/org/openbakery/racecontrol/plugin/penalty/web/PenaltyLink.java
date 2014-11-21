package org.openbakery.racecontrol.plugin.penalty.web;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.plugin.penalty.data.Penalty.Type;

public class PenaltyLink extends Link<Driver> {

	private static final long serialVersionUID = 1L;

	private Type type;

	public PenaltyLink(String id, IModel<Driver> object, Type type) {
		super(id, object);
		this.type = type;
	}

	@Override
	public void onClick() {
		Driver driver = getModel().getObject();
		((PenaltyPage) getPage()).sendPenalty(driver, type);
	}

}
