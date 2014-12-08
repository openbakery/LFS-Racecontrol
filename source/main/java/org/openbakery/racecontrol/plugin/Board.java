package org.openbakery.racecontrol.plugin;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.openbakery.jinsim.Car;
import org.openbakery.jinsim.Tiny;
import org.openbakery.jinsim.Track;
import org.openbakery.jinsim.response.ConnectionLeaveResponse;
import org.openbakery.jinsim.response.HiddenMessageResponse;
import org.openbakery.jinsim.response.InSimResponse;
import org.openbakery.jinsim.response.TinyResponse;
import org.openbakery.jinsim.types.InSimTime;

import org.openbakery.racecontrol.Race;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.event.LapEvent;
import org.openbakery.racecontrol.event.LapEventListener;
import org.openbakery.racecontrol.event.RaceEvent;
import org.openbakery.racecontrol.event.RaceEventListener;
import org.openbakery.racecontrol.gui.Button;
import org.openbakery.racecontrol.gui.Panel;
import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.web.bean.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Board implements Plugin, LapEventListener, RaceEventListener {

	private static Logger log = LoggerFactory.getLogger(Board.class);

	private static final int POSITION_Y = 55;

	private static final int POSITION_X = 1;

	private static final int POSITION_Y_HELP = 90;

	private static final int ROWS = 3;

	private static final int PERSONAL_BEST_ROW = ROWS + 2;

	private static final int SERVER_RECORD_ROW = ROWS + 3;

	private HashMap<Integer, Panel> lapTimePanelMap;

	private HashMap<Car, Lap> serverBestMap;

	private int splits = 4;

	private Persistence persistence;

	private Track track;

	Button recordBy;

	private Race race;

	public Board(Persistence persistence) {
		lapTimePanelMap = new HashMap<Integer, Panel>();
		serverBestMap = new HashMap<Car, Lap>();
		this.persistence = persistence;
		// try {
		// JInSimClient.getInstance().send(new TinyRequest(Tiny.SEND_STATE_INFO));
		// } catch (IOException e) {
		// log.error(e.getMessage(), e);
		// }
	}

	public void packetReceived(InSimResponse response) {
		if (response instanceof HiddenMessageResponse) {
			HiddenMessageResponse hiddenMessageResonse = (HiddenMessageResponse) response;
			if ("board".equalsIgnoreCase(hiddenMessageResonse.getMessage())) {
				Driver driver = getDriver(hiddenMessageResonse.getConnectionId());
				if (driver != null) {
					showStats(driver);
				}
			}
		} else if (response instanceof ConnectionLeaveResponse) {
			int connectionId = Integer.valueOf(((ConnectionLeaveResponse) response).getConnectionId());
			Panel panel = lapTimePanelMap.get(connectionId);
			if (panel != null) {
				panel.destroy();
			}
			lapTimePanelMap.remove(connectionId);
			log.debug("remove player: {}", ((ConnectionLeaveResponse) response).getConnectionId());
		} else if (response instanceof TinyResponse) {
			TinyResponse tinyResponse = (TinyResponse) response;
			Tiny type = tinyResponse.getType();
			if (type == Tiny.MULTIPLAYER_END || type == Tiny.RACE_END) {
				for (Panel panel : lapTimePanelMap.values()) {
					panel.destroy();

				}
				lapTimePanelMap.clear();
			}
		}
	}

	private Driver getDriver(int connectionId) {
		if (race != null) {
			return race.getDriver(connectionId);
		}
		return null;
	}

	private void updatePanels() {
		if (race != null) {
			for (Driver driver : race.getRaceEntry().getDrivers()) {
				Panel panel = lapTimePanelMap.get(driver.getConnectionId());
				if (panel != null) {
					setPanelHeader(panel, driver);
					// setBestTimes(driver);
				}
			}
		}
	}

	private void setPanelHeader(Panel panel, Driver driver) {
		int connectionId = driver.getConnectionId();
		try {
			boolean isVisible = panel.isVisible();
			panel.clear();

			setPanelColumns(panel);
			Button button;
			button = new Button(connectionId, "Lap");
			panel.add(button, 0, 0);

			int i = 1;
			for (; i < splits + 1; i++) {
				button = new Button(connectionId, "SP" + (i));
				panel.add(button, i, 0);
			}
			button = new Button(connectionId, "Time");
			panel.add(button, i++, 0);

			button = new Button(connectionId, "Gap");
			panel.add(button, i, 0);
			panel.setVisible(isVisible);
		} catch (IOException e) {
			log.error("Visiblity error", e);
		}

	}

	private void setPanelColumns(Panel panel) {
		int[] columns = new int[splits + 3];
		for (int i = 1; i < columns.length; i++) {
			columns[i] = 7;
		}
		columns[0] = 5;
		columns[columns.length - 2] = 10;

		panel.setColumns(columns);
	}

	public void lapSplit(LapEvent event) {
		Driver driver = event.getDriver();
		int connectionId = driver.getConnectionId();
		Panel panel = lapTimePanelMap.get(connectionId);
		if (panel != null) {
			Lap lap = event.getLap();
			if (event.getSplit() == 1) {
				// push down the buttons;
				for (int y = ROWS; y > 1; y--) {
					Lap lapCurrent = getLap(connectionId, y - 1);
					Lap lapPrevious = getLap(connectionId, y);
					setLapInRow(connectionId, y, lapCurrent, lapPrevious);
				}
			}

			setLapInRow(connectionId, 1, lap, null);
		}
	}

	public void lapFinished(LapEvent event) {

		Driver driver = event.getDriver();

		Lap lap = event.getLap();

		Lap lapPrevious = getLap(driver.getConnectionId(), 2);
		setLapInRow(driver.getConnectionId(), 1, lap, lapPrevious);
		// updateBestTimes(driver, lap);
	}

	private void addNewPlayer(Driver driver) {
		log.debug("add new player with connectionId: {}", driver);
		if (!lapTimePanelMap.containsKey(driver.getConnectionId())) {
			Panel panel = new Panel(POSITION_X, POSITION_Y);
			lapTimePanelMap.put(driver.getConnectionId(), panel);
			setPanelHeader(panel, driver);
		}
	}

	private void showStats(Driver driver) {
		log.debug("display board for {}", driver);
		if (!lapTimePanelMap.containsKey(driver.getConnectionId())) {
			addNewPlayer(driver);
		}

		Panel panel = lapTimePanelMap.get(driver.getConnectionId());
		try {
			panel.setVisible(!panel.isVisible());
			// if (recordBy != null) {
			// recordBy.setVisible(panel.isVisible());
			// }

			// if (panel.isVisible()) {
			// Button help = new Button(driver.getConnectionId(), "REC: Best lap on server, SPB = Personal best on server", POSITION_X, POSITION_Y_HELP, 60, 5);
			// help.setHideTime(5000);
			// try {
			// help.setVisible(true);
			// } catch (IOException e) {
			// log.error("Unable to set help visible", e);
			// }
			// }

		} catch (IOException e) {
			log.error("Error hiding panel for driver " + driver, e);
		}

	}

	// private void setBestTimes(Driver driver) {
	// QueryHelper helper = new QueryHelper(persistence);
	//
	// /*
	// * if (driver.getName() == null) { driver.setName("Brilwing"); }
	// */
	//
	// if (track != null && driver.getName() != null && driver.getCarName() != null) {
	// Car car = Car.getCarByName(driver.getCarName());
	// Lap serverBest = serverBestMap.get(car);
	// if (serverBest == null) {
	// serverBest = helper.getFastestLapOnServer(track, car);
	// }
	// if (serverBest != null) {
	// serverBestMap.put(car, serverBest);
	// }
	//
	// Lap lap = helper.getFastestLapOnServerForDriver(track, driver);
	// setLapInRow(driver.getConnectionId(), PERSONAL_BEST_ROW, null, null, "SPB", true);
	// // updateBestTimes(driver, lap);
	// }
	// }

	// private void updateBestTimes(Driver driver, Lap currentLap) {
	//
	// int connectionId = driver.getConnectionId();
	// if (driver.getCarName() == null) {
	// return;
	// }
	// Car car = Car.getCarByName(driver.getCarName());
	// Lap serverBest = serverBestMap.get(car);
	// if (serverBest == null || (currentLap != null && serverBest.getTime() > currentLap.getTime())) {
	// serverBest = currentLap;
	// serverBestMap.put(car, serverBest);
	// }
	// setLapInRow(connectionId, SERVER_RECORD_ROW, serverBest, currentLap, "REC", true);
	// String by = "";
	// if (serverBest != null) {
	// Driver driverBest = serverBest.getDriver();
	// if (driverBest != null) {
	// by = "by " + driverBest.getName();
	// }
	// }
	//
	// if (recordBy == null) {
	// recordBy = new Button(connectionId, by, 6, POSITION_Y + 28, 21, 4);
	// recordBy.setTextAlign(TextAlign.left);
	// } else {
	// recordBy.setText(by);
	// }
	//
	// Lap lap = getLap(connectionId, PERSONAL_BEST_ROW);
	// if (lap == null || (currentLap != null && lap.getTime() > currentLap.getTime())) {
	// lap = currentLap;
	// }
	// setLapInRow(connectionId, PERSONAL_BEST_ROW, lap, currentLap, "SPB", true);
	// }

	private Lap getLap(int connectionId, int row) {
		Panel panel = lapTimePanelMap.get(connectionId);
		if (panel != null) {
			Button button = panel.get(0, row);
			if (button != null) {
				return (Lap) button.getObject();
			}
		}
		return null;
	}

	private void setLapInRow(int connectionId, int row, Lap currentLap, Lap previousLap) {
		if (currentLap == null) {
			setLapInRow(connectionId, row, currentLap, previousLap, "", false);
		} else {
			setLapInRow(connectionId, row, currentLap, previousLap, Integer.toString(currentLap.getNumber()), false);
		}
	}

	private void setLapInRow(int connectionId, int row, Lap currentLap, Lap previousLap, String label, boolean inverseGap) {
		Panel panel = lapTimePanelMap.get(connectionId);
		if (panel == null) {
			return;
		}

		Button button = panel.get(0, row);
		if (label == null) {
			label = "";
		}
		if (button != null) {
			button.setText(label);
		} else {
			button = new Button(connectionId, label);
			panel.add(button, 0, row);
		}
		button.setObject(currentLap);

		for (int i = 0; i < splits; i++) {
			String split = "";
			if (currentLap != null && currentLap.getSplit(i) > 0) {
				split = InSimTime.toString(currentLap.getSplit(i));
			}
			button = panel.get(i + 1, row);
			if (button != null) {
				button.setText(split);
			} else {
				button = new Button(connectionId, split);
				panel.add(button, i + 1, row);
			}
		}

		button = panel.get(splits + 1, row);
		String time = "";
		if (currentLap != null && currentLap.getTime() > 0) {
			time = InSimTime.toString(currentLap.getTime());
		}
		if (button != null) {
			button.setText(time);
		} else {
			button = new Button(connectionId, time);
			panel.add(button, splits + 1, row);
		}

		String gap = "";
		if (previousLap != null && currentLap != null && previousLap.getTime() > 0 && currentLap.getTime() > 0) {
			if (inverseGap) {
				gap = InSimTime.toString(currentLap.getTime() - previousLap.getTime(), true);
			} else {
				gap = InSimTime.toString(previousLap.getTime() - currentLap.getTime(), true);
			}
		}
		button = panel.get(splits + 2, row);
		if (button != null) {
			button.setText(gap);
		} else {
			button = new Button(connectionId, gap);
			panel.add(button, splits + 2, row);
		}
	}

	public String getHelp() {
		return "Displays the last 3 lap times";
	}

	public String getName() {
		return "Board";
	}

	public void raceEndEvent(RaceEvent event) {
		this.race = null;
	}

	public void raceStartEvent(RaceEvent event) {
		track = Track.getTrackByShortName(event.getRace().getRaceEntry().getTrack());
		splits = track.getSplits();
		this.race = event.getRace();
		updatePanels();
	}

	public void raceNewDriverEvent(RaceEvent event) {
		addNewPlayer(event.getDriver());
		// setBestTimes(event.getDriver());
	}

	public List<MenuItem> getMenuItems() {
		return Collections.emptyList();
	}

}
