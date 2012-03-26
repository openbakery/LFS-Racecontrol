package org.openbakery.racecontrol.service;

import org.openbakery.racecontrol.JInSimClient;
import org.openbakery.racecontrol.RaceControl;

public class RaceService {

	private JInSimClient client;

	private RaceControl raceControl;

	public JInSimClient getClient() {
		return client;
	}

	public void setClient(JInSimClient client) {
		this.client = client;
	}

	public RaceControl getRaceControl() {
		return raceControl;
	}

	public void setRaceControl(RaceControl raceControl) {
		this.raceControl = raceControl;
	}

}
