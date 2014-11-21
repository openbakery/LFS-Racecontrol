package org.openbakery.racecontrol.plugin.tracker.web;

import org.apache.wicket.markup.html.form.Button;
import org.openbakery.racecontrol.web.RaceControlSession;

public class SaveButton extends Button {

	private RaceControlSession session;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2861490813761189543L;

	public SaveButton(RaceControlSession session) {
		super("save");
		this.session = session;
	}

	@Override
	public void onSubmit() {
		session.getServiceLocator().getSettingsService().storeAll();
		setResponsePage(TrackerPage.class);
	}

}
