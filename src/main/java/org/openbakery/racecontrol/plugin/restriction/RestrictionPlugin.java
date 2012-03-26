package org.openbakery.racecontrol.plugin.restriction;

import java.util.Arrays;
import java.util.List;

import net.sf.jinsim.response.InSimResponse;

import org.openbakery.racecontrol.plugin.Plugin;
import org.openbakery.racecontrol.plugin.restriction.web.RestictionsPage;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestrictionPlugin implements Plugin {

	private static Logger logger = LoggerFactory.getLogger(RestrictionPlugin.class);

	public String getHelp() {
		return "Plugin to manage restrictions";
	}

	public List<MenuItem> getMenuItems() {
		return Arrays.asList(new MenuItem("Restictions", RestictionsPage.class, Visibility.AUTHENTICATED, 0));
	}

	public String getName() {
		return "Restrictions";
	}

	public void packetReceived(InSimResponse response) {
		// TODO Auto-generated method stub

	}

}
