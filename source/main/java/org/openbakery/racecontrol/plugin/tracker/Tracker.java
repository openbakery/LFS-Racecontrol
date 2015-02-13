package org.openbakery.racecontrol.plugin.tracker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.openbakery.jinsim.Tiny;
import org.openbakery.jinsim.Track;
import org.openbakery.jinsim.request.MessageRequest;
import org.openbakery.jinsim.response.ButtonClickedResponse;
import org.openbakery.jinsim.response.ButtonTypeResponse;
import org.openbakery.jinsim.response.HiddenMessageResponse;
import org.openbakery.jinsim.response.InSimResponse;
import org.openbakery.jinsim.response.TinyResponse;
import org.openbakery.jinsim.types.InSimTime;

import org.openbakery.racecontrol.JInSimClient;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.event.LapEvent;
import org.openbakery.racecontrol.event.LapEventListener;
import org.openbakery.racecontrol.gui.Button;
import org.openbakery.racecontrol.gui.Panel;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.openbakery.racecontrol.persistence.QueryHelper;
import org.openbakery.racecontrol.persistence.bean.Profile;
import org.openbakery.racecontrol.plugin.Plugin;
import org.openbakery.racecontrol.plugin.tracker.data.TrackerSettings;
import org.openbakery.racecontrol.plugin.tracker.web.TrackerPage;
import org.openbakery.racecontrol.plugin.tracker.web.TrackerSettingsPage;
import org.openbakery.racecontrol.service.SettingsService;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.openbakery.racecontrol.web.bean.Visibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Tracker implements Plugin, LapEventListener {

	private static Logger log = LoggerFactory.getLogger(Tracker.class);

	private HashMap<Integer, Panel> trackerPanel;

	@Autowired
	private TrackerService trackerService;

	private RaceControl raceControl;

	@Autowired
	private QueryHelper queryHelper;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private JInSimClient insimClient;

	public Tracker() {
		trackerPanel = new HashMap<Integer, Panel>();
	}

	@Autowired
	void setRaceControl(RaceControl raceControl) {
		this.raceControl = raceControl;
		raceControl.addLapEventListener(this);
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
					Driver driver = raceControl.getRace().getDriver(connectionId);

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
		} /*else if (response instanceof PitLaneResponse) {



		}*/
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
					String message = button.getText();
					if (response instanceof ButtonTypeResponse) {
						message += " - " + ((ButtonTypeResponse) response).getTypeInText();
					}
					log.debug("send button message: {}", message);

					sendMessage(message);
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

	@Override
	public void lapFinished(LapEvent event) {

		TrackerSettings settings = settingsService.getTrackerSettings();

		Lap lap = queryHelper.getFastestLapOnServerForDriver(settings.getCars(), settings.getTrack(), event.getDriver().getName(), settings.getNumberLaps());

		SimpleDateFormat dateFormat = new SimpleDateFormat("m:ss.SSS");
		if (event.getLap().getTime() == lap.getTime()) {
			sendMessage("New fastest lap for " + event.getDriver().getName() + ": " + dateFormat.format(lap.getTime()));
			// new fastest lap;
		}

		int numberLaps = queryHelper.getNumberLapsOnServerForDriver(settings.getTrack(), event.getDriver());
		sendMessage(event.getDriver().getName() + ": " + numberLaps + " of " + settings.getNumberLaps() + " completed");

	}

	@Override
	public void lapSplit(LapEvent event) {

	}


	void sendMessage(String message) {
		MessageRequest messageRequest = new MessageRequest();
		messageRequest.setMessage(message);
		try {
			insimClient.send(messageRequest);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}


	}


}
