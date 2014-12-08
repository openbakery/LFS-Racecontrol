package org.openbakery.racecontrol.plugin;

import java.util.List;

import org.openbakery.jinsim.response.InSimListener;

import org.openbakery.racecontrol.web.bean.MenuItem;

public interface Plugin extends InSimListener {

	public String getName();

	public String getHelp();

	public List<MenuItem> getMenuItems();
}
