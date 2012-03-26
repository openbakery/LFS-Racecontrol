package org.openbakery.racecontrol.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import net.sf.jinsim.response.ButtonTypeResponse;
import net.sf.jinsim.response.HiddenMessageResponse;
import net.sf.jinsim.response.InSimListener;
import net.sf.jinsim.response.InSimResponse;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.gui.Button;
import org.openbakery.racecontrol.gui.MessageHelper;
import org.openbakery.racecontrol.gui.Panel;

public class PrivateMessage implements InSimListener {

	private final List<Driver> drivers;

	private HashMap<Driver, Panel> panelMap;

	public PrivateMessage(final List<Driver> drivers) {
		this.drivers = drivers;
		panelMap = new HashMap<Driver, Panel>();
	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof HiddenMessageResponse) {
			HiddenMessageResponse hiddenMessageResonse = (HiddenMessageResponse) response;
			if ("pm".equalsIgnoreCase(hiddenMessageResonse.getMessage())) {
				showDrivers(hiddenMessageResonse.getConnectionId());
			}
		} else if (response instanceof ButtonTypeResponse) {
			sendMessage((ButtonTypeResponse) response);
		}
	}

	private void sendMessage(ButtonTypeResponse response) {
		System.out.println("button clicked: " + response.getClickId() + " - " + response.getTypeInText());
		Driver currentDriver = getCurrentDriver(response.getConnectionId());
		if (currentDriver != null) {
			Panel panel = panelMap.get(currentDriver);

			Button button = panel.getButton(response.getClickId());
			if (button != null) {
				Object object = button.getObject();
				if (object != null && object instanceof Number) {
					byte connectionId = ((Number) button.getObject()).byteValue();
					MessageHelper.sendMessage(connectionId, currentDriver.getName() + ": " + response.getTypeInText());
				}
			}
			try {
				panel.setVisible(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void showDrivers(int connectionId) {
		Driver currentDriver = getCurrentDriver(connectionId);
		if (currentDriver != null) {
			Panel panel = new Panel(1, 20);
			panelMap.put(currentDriver, panel);
			for (int i = 0; i < 5; i++) {
				for (Driver driver : drivers) {
					String name = driver.getName();
					if (name == null) {
						name = "" + driver.getPlayerId();
					}
					Button button = new Button(connectionId, name);
					button.setTypeIn(true);
					button.setObject(Byte.valueOf((byte) driver.getConnectionId()));
					panel.add(button);
				}
			}
			try {
				panel.setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Driver getCurrentDriver(int connectionId) {
		for (Driver driver : drivers) {
			if (driver.getConnectionId() == connectionId) {
				return driver;
			}
		}
		return null;
	}
}
