package org.openbakery.racecontrol.plugin.tracker;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.sf.jinsim.Tiny;
import net.sf.jinsim.Track;
import net.sf.jinsim.request.MessageRequest;
import net.sf.jinsim.response.ButtonClickedResponse;
import net.sf.jinsim.response.ButtonTypeResponse;
import net.sf.jinsim.response.HiddenMessageResponse;
import net.sf.jinsim.response.InSimResponse;
import net.sf.jinsim.response.TinyResponse;
import net.sf.jinsim.types.InSimTime;

import org.openbakery.racecontrol.JInSimClient;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.gui.Button;
import org.openbakery.racecontrol.gui.Panel;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.persistence.bean.Profile;
import org.openbakery.racecontrol.plugin.Plugin;
import org.openbakery.racecontrol.plugin.tracker.web.TrackerPage;
import org.openbakery.racecontrol.plugin.tracker.web.TrackerSettingsPage;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Tracker implements Plugin {

	private static Logger log = LoggerFactory.getLogger(Tracker.class);

	private HashMap<Integer, Panel> trackerPanel;

	@Autowired
	private TrackerService trackerService;

	@Autowired
	private RaceControl raceControl;

	public Tracker() {
		trackerPanel = new HashMap<Integer, Panel>();
	}

	public String getHelp() {
		return "Provided commands by the '" + getName() + "' plugin are: tracker display the tracker";
	}

	public String getName() {
		return "Tracker";
	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof HiddenMessageResponse) {
			HiddenMessageResponse hiddenMessageResponse = (HiddenMessageResponse) response;
			int connectionId = hiddenMessageResponse.getConnectionId();
			String message = hiddenMessageResponse.getMessage();

			if (message.startsWith("tracker")) {
				try {
					Driver driver = raceControl.getRace().getDriver(connectionId, "");

					showTracker(new Integer(connectionId), driver.isAdmin());
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		} else if (response instanceof ButtonClickedResponse) {
			processClickResponse((ButtonClickedResponse) response);
		} else if (response instanceof TinyResponse) {
			TinyResponse tinyResponse = (TinyResponse) response;
			Tiny type = tinyResponse.getType();
			if (type == Tiny.MULTIPLAYER_END || type == Tiny.RACE_END) {
				for (Panel panel : trackerPanel.values()) {
					panel.destroy();
				}
				trackerPanel.clear();
			}
		}
	}

	private void processClickResponse(ButtonClickedResponse response) {
		Panel panel = trackerPanel.get(new Integer(response.getConnectionId()));
		if (panel != null) {

			Button closeButton = panel.get(0, panel.getRowCount(1));
			if (response.getClickId() == closeButton.getId()) {
				trackerPanel.remove(new Integer(response.getConnectionId()));
				panel.destroy();
				return;
			}

			for (int i = 0; i < panel.getRowCount(1); i++) {
				Button button = panel.get(1, i);
				if (button != null && button.getId() == response.getClickId()) {
					MessageRequest msgRequest = new MessageRequest();
					String message = button.getText();
					if (response instanceof ButtonTypeResponse) {
						message += " - " + ((ButtonTypeResponse) response).getTypeInText();
					}
					log.debug("send button message: {}", message);
					msgRequest.setMessage(message);
					try {
						JInSimClient.getInstance().send(msgRequest);
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	private void showTracker(Integer connectionId, boolean isAdmin) throws IOException {
		log.debug("show tracker connectionId = {}, isAdmin = {}", connectionId, isAdmin);
		Panel panel = trackerPanel.get(connectionId);
		if (panel != null) {
			log.debug("tracker panel found, so hide it");
			trackerPanel.remove(connectionId);
			panel.destroy();
			return;
		}
		panel = new Panel(20, 40);
		panel.setColumns(10, 40, 15, 10, 15, 15);

		try {
			List<Lap> lapList = trackerService.getFastestLap();
			List<Profile> profiles = trackerService.getSignedUpDrivers();
			Track track = trackerService.getTrack();

			int i = 1;
			Lap previous = null;
			for (Lap lap : lapList) {
				panel.add(new Button(connectionId, Integer.toString(i++)), 0);
				String name = lap.getDriver().getName();
				for (Profile profile : profiles) {
					if (profile.getLfsworldName().equalsIgnoreCase(lap.getDriver().getName())) {
						name = profile.getFullName();
					}
				}
				Button nameButton = new Button(connectionId, name);
				if (isAdmin) {
					nameButton.setClickable(this);
					nameButton.setTypeIn(true);
				}
				panel.add(nameButton, 1);
				panel.add(new Button(connectionId, InSimTime.toString(lap.getTime())), 2);
				if (previous == null || lap.getTime() == 0) {
					panel.add(new Button(connectionId, ""), 3);
				} else {
					int gap = previous.getTime() - lap.getTime();
					panel.add(new Button(connectionId, InSimTime.toString(gap, true)), 3);
				}

				int time = 0;
				for (int j = 0; j < track.getSplits(); j++) {
					time += lap.getSplit(j);
					panel.add(new Button(connectionId, InSimTime.toString(time)), 4 + j);
				}

				previous = lap;
			}

		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}
		Button closeButton = new Button(connectionId, "Close");
		closeButton.setClickable(this);
		panel.add(closeButton, 0);
		panel.setVisible(true);
		trackerPanel.put(connectionId, panel);
	}

	public List<MenuItem> getMenuItems() {
		return Arrays.asList(new MenuItem("Tracker", TrackerPage.class, -1), new MenuItem("Tracker Settings", TrackerSettingsPage.class, Visibility.AUTHENTICATED, 2));
	}

}
