package org.openbakery.racecontrol.plugin.restriction.web;

import org.apache.wicket.markup.html.form.Button;
import org.openbakery.racecontrol.plugin.restriction.data.Restrictions;
import org.openbakery.racecontrol.plugin.restriction.service.RestrictionsService;
import org.openbakery.racecontrol.web.RaceControlSession;

public class SaveButton extends Button {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Restrictions restrictions;

	public SaveButton(String id, Restrictions restrictions) {
		super(id);
		this.restrictions = restrictions;
	}

	@Override
	public void onSubmit() {
		RaceControlSession session = (RaceControlSession) getSession();
		RestrictionsService service = session.getService(RestrictionsService.class);
		service.setRestrictions(restrictions);
		setResponsePage(RestictionsPage.class);
	}
}
