package org.openbakery.racecontrol.plugin;

import java.util.Collections;
import java.util.List;

import net.sf.jinsim.response.HiddenMessageResponse;
import net.sf.jinsim.response.InSimResponse;

import org.openbakery.racecontrol.web.bean.MenuItem;

public class ChatWarning implements Plugin {

	public String getHelp() {
		return "The ChatWarning sends a warning to the user when he chat and disconnects the player if he spams";
	}

	public String getName() {
		return "ChatWarning";
	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof HiddenMessageResponse) {
			processHiddenMessageResponse((HiddenMessageResponse) response);
		}
		// TODO Auto-generated method stub

	}

	private void processHiddenMessageResponse(HiddenMessageResponse response) {

	}

	public List<MenuItem> getMenuItems() {
		return Collections.emptyList();
	}

}
