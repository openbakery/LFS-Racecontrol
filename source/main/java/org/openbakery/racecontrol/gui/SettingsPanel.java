package org.openbakery.racecontrol.gui;

import java.util.Map;

public class SettingsPanel {

	private Map<String, Object>settings;
	private Panel panel;
	
	public SettingsPanel() {
	}
	
	public void setSettings(Map<String, Object> settings) {
		this.settings = settings;
	}


	public Map<String, Object>getSettings() {
		return settings;
	}
	
	public void setVisible(boolean visible) {
		if (visible) {
			
		}
	}
	
	private Panel createSettingsPanel() {
		Panel panel = new Panel(10, 10);
		return panel;
	}
	
}
