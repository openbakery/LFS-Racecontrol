package org.openbakery.racecontrol.plugin.tracker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.openbakery.jinsim.Car;
import org.openbakery.jinsim.Tiny;
import org.openbakery.jinsim.Track;
import org.openbakery.jinsim.request.MessageRequest;
import org.openbakery.jinsim.response.*;
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

	SimpleDateFormat dateFormat = new SimpleDateFormat("m:ss.SSS");

	@Autowired
	private TrackerService trackerService;

	private RaceControl raceControl;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private JInSimClient insimClient;

	private Lap fastestLap;

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

		if (response instanceof NewPlayerResponse) {
			NewPlayerResponse newPlayerResponse = (NewPlayerResponse)response;
			try {
				showPanel(newPlayerResponse.getConnectionId());
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		} else if (response instanceof ConnectionLeaveResponse) {
			ConnectionLeaveResponse connectionLeaveResponse = (ConnectionLeaveResponse)response;
			hidePanel(connectionLeaveResponse.getConnectionId());
		} else if (response instanceof PitLaneResponse) {
			Driver driver = raceControl.getRace().getDriverByPlayerId(((PitLaneResponse) response).getPlayerId());
			updatePanel(driver.getConnectionId(), null);
		}

	}

	private void hidePanel(Integer connectionId) {
		log.debug("hidePanel for {}", connectionId);
		Panel panel = trackerPanel.get(connectionId);
		if (panel != null) {
			trackerPanel.remove(connectionId);
			panel.destroy();
		}
	}

	private void showPanel(Integer connectionId) throws IOException {
		// destroy the panel if one exists for this connectionID
		hidePanel(connectionId);
		log.debug("showPanel for {}", connectionId);

		Panel panel = new Panel(135, 4);
		panel.setColumns(25);
		panel.add(new Button(connectionId, ""), 0);
		panel.add(new Button(connectionId, ""), 0);
		Button infoButton = new Button(connectionId, "");
		infoButton.setVisible(false);
		panel.add(infoButton, 0);

		updatePanel(connectionId, null);

		panel.setVisible(true);
		trackerPanel.put(connectionId, panel);
		}

	public void updatePanel(Integer connectionId, Lap currentLap) {
		log.debug("update panel for {}", connectionId);
		log.debug("update panel with lap {}", currentLap);

		Panel panel = trackerPanel.get(connectionId);
		if (panel == null) {
			log.debug("Panel not found for connectionId {}, so do nothing", connectionId);
			return;
		}

		Driver driver = raceControl.getRace().getDriver(connectionId);
		if (driver == null) {
			log.debug("Driver not found for connectionId {}, so do nothing", connectionId);
			return;
		}

		Lap lap = getFastestLapForDriver(driver);

		Button numberLapsButton = panel.get(0, 0);
		//int numberLaps = getNumberLapsOnServerForDriver(driver);
		int lapsLeft = settingsService.getTrackerSettings().getNumberLaps() - lap.getAttempt();
		if (lap.getPosition() > 0) {
			numberLapsButton.setText("Laps left: " + lapsLeft + " - Pos: " + lap.getPosition());
		} else {
			numberLapsButton.setText("Laps left: " + lapsLeft);
		}

		Button lapTimeButton = panel.get(0, 1);
		lapTimeButton.setText("Fastest Lap: " + InSimTime.toString(lap.getTime()));

		try {
			Button infoButton = panel.get(0, 2);
			if (currentLap != null) {

				StringBuilder gapString = new StringBuilder();
				int i = 0;
				while(lap.getSplit(i) > 0) {

					if (i > 0) {
						gapString.append(" / ");
					}
					int gap = fastestLap.getSplit(i) - lap.getSplit(i);

					gapString.append(InSimTime.toString(gap, true));
					i++;
				}

				if (lap.isFinished()) {
					gapString.append(" : ");
					int gap = fastestLap.getTime() - lap.getTime();
					gapString.append(InSimTime.toString(gap, true));
				}

				if (gapString.length() > 0) {
					infoButton.setText(gapString.toString());
				}
				infoButton.setVisible(gapString.length() > 0);

			} else {
				infoButton.setVisible(false);
			}
		} catch (IOException ex) {
			log.error(ex.getMessage(), ex);
		}


	}


	public List<MenuItem> getMenuItems() {
		return Arrays.asList(new MenuItem("Tracker", TrackerPage.class, -1), new MenuItem("Tracker Settings", TrackerSettingsPage.class, Visibility.AUTHENTICATED, 2));
	}

	@Override
	public void lapFinished(LapEvent event) {
		log.debug("lapFinished");
		updatePanel(event.getDriver().getConnectionId(), event.getLap());
	}



	private Lap getFastestLapForDriver(Driver driver) {

		TrackerSettings settings = settingsService.getTrackerSettings();
		try {
			List<Lap>lapList = trackerService.getFastestLap(settings.getTrack(), settings.getCars(), settings.getNumberLaps());

			log.debug("lapList {}", lapList);

			if (lapList.size() > 0) {
				log.debug("==> set fastest lap: {}", fastestLap);
				fastestLap = lapList.get(0);
			}

			log.debug("==== DRIVER  {}", driver);
			for (Lap lap : lapList) {
				log.debug("lap.driver {}", lap.getDriver());

				if (lap.getDriver().getName().equals(driver.getName())) {
					log.debug("getFastestLapForDriver found: {}", lap);
					return lap;
				}
			}

		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}
		return new Lap();

	}


	@Override
	public void lapSplit(LapEvent event) {
		log.debug("lapSplit");
		updatePanel(event.getDriver().getConnectionId(), event.getLap());
	}


}
