package org.openbakery.racecontrol.plugin.live;

import java.util.Arrays;
import java.util.List;

import org.openbakery.jinsim.response.InSimResponse;

import org.openbakery.racecontrol.plugin.Plugin;
import org.openbakery.racecontrol.plugin.live.web.LivePage;
import org.openbakery.racecontrol.web.bean.MenuItem;

public class LivePlugin implements Plugin {

	public String getHelp() {
		return "This plugin is a live tracker of LFS races";
	}

	public List<MenuItem> getMenuItems() {
		return Arrays.asList(new MenuItem("Live", LivePage.class, -1));
	}

	public String getName() {
		return "Live";
	}

	public void packetReceived(InSimResponse response) {
	}

}
