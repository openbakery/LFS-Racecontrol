package org.openbakery.racecontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.openbakery.jinsim.Tiny;
import org.openbakery.jinsim.request.TinyRequest;
import org.openbakery.jinsim.response.*;

import org.openbakery.racecontrol.control.AbstractControl;
import org.openbakery.racecontrol.control.LapControl;
import org.openbakery.racecontrol.control.MainRaceControl;
import org.openbakery.racecontrol.control.MessageControl;
import org.openbakery.racecontrol.control.PitControl;
import org.openbakery.racecontrol.control.PlayerControl;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.Flag;
import org.openbakery.racecontrol.data.Lap;
import org.openbakery.racecontrol.event.CameraEvent;
import org.openbakery.racecontrol.event.CameraEventListener;
import org.openbakery.racecontrol.event.LapEvent;
import org.openbakery.racecontrol.event.LapEventListener;
import org.openbakery.racecontrol.event.RaceEvent;
import org.openbakery.racecontrol.event.RaceEventListener;
import org.openbakery.racecontrol.event.RaceEvent.Type;
import org.openbakery.racecontrol.gui.ButtonMessageHelper;
import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RaceControl implements InSimListener, Runnable {

	private static Logger log = LoggerFactory.getLogger(RaceControl.class);

	private JInSimClient client;

	private Persistence persistence;

	private Race race;

	private ArrayList<LapEventListener> lapEventListener = new ArrayList<LapEventListener>();

	private ArrayList<RaceEventListener> raceEventListener = new ArrayList<RaceEventListener>();

	private ArrayList<CameraEventListener> cameraEventListener = new ArrayList<CameraEventListener>();

	private List<Plugin> plugins;

	private ArrayList<AbstractControl> controlList;

	public RaceControl() {
		race = new Race();
		plugins = new ArrayList<Plugin>();
	}

	public void start() throws IOException {
		controlList = new ArrayList<AbstractControl>();
		controlList.add(new MainRaceControl(this, persistence));
		controlList.add(new LapControl(this, persistence));
		controlList.add(new MessageControl(this, persistence));
		controlList.add(new PitControl(this, persistence));
		controlList.add(new PlayerControl(this, persistence));

		client.connect();
		client.addListener(this);
		log.info("Started...");
		client.send(new TinyRequest(Tiny.RESTART));
		client.send(new TinyRequest(Tiny.ALL_CONNECTIONS));
		client.send(new TinyRequest(Tiny.ALL_PLAYERS));
	}

	public void stop() throws IOException {
    log.info("Stopped");

		for (AbstractControl control : controlList) {
			control.destroy();
		}
		controlList.clear();
		client.removeListener(this);
		client.close();

	}


	public boolean isConnected() {
		return client.isConnected();
	}

	public void packetReceived(InSimResponse response) {
		log.debug("packageReceived: {}", response);

		if (response instanceof PlayerResponse) {
			PlayerResponse playerResponse = (PlayerResponse)response;
			playerResponse.getPlayerId();
			Driver driver = race.getDriverByPlayerId(playerResponse.getPlayerId());
			if (driver == null) {
				log.debug("Driver unknown: {}", playerResponse);
			} else {
				log.debug("Driver {}: {}", driver.getName(), playerResponse);
			}

		}

    for (AbstractControl control : controlList) {
			control.packetReceived(response);
		}
		try {
			if (response instanceof FlagResponse) {
				processFlagResponse((FlagResponse) response);
			} else if (response instanceof PenaltyResponse) {
				processPenaltyResponse((PenaltyResponse) response);
			} else if (response instanceof MultiplayerBeginResponse) {
				processMultiplayerBeginResponse((MultiplayerBeginResponse) response);
			} else if (response instanceof HiddenMessageResponse) {
				HiddenMessageResponse hiddenMessageResponse = (HiddenMessageResponse) response;
				int connectionId = hiddenMessageResponse.getConnectionId();
				Driver driver = race.getDriver(connectionId);
				if (connectionId == 0 || driver.isAdmin()) {
					processCommand(hiddenMessageResponse);
				}
			} else if (response instanceof StateResponse) {
				StateResponse cameraResponse = (StateResponse) response;
				Driver driver = race.getDriverByPlayerId(cameraResponse.getPlayer());
				if (driver != null) {
					notifyCameryEventListener(new CameraEvent(driver, CameraEvent.CameraType.getByInSimType(cameraResponse.getCamera())));
				}
			} else if (response instanceof ConnectionCloseResponse) {
				stop();
				log.info("Disconnected from host");
				//Thread.sleep(10000);
				//start();
			}
			// else {
			// log.debug("reponse: " + response);
			// }
		} catch (Exception ex) {
			log.error("Unknown error", ex);
			System.exit(1);
		}

	}

	private void processMultiplayerBeginResponse(MultiplayerBeginResponse response) {
		try {
			client.send(new TinyRequest(Tiny.ALL_CONNECTIONS));
			client.send(new TinyRequest(Tiny.ALL_PLAYERS));
			client.send(new TinyRequest(Tiny.SEND_STATE_INFO));
		} catch (IOException e) {
			log.error("Cannot end all players request", e);
		}

	}

	private void processPenaltyResponse(PenaltyResponse response) {
		Driver driver;
		try {
			driver = race.getRaceDriver(response);
			if (!race.hasFinished(driver)) {
				Lap lap = driver.getCurrentLap();
				lap.setOldPenalty(response.getOldPenalty());
				lap.setNewPenalty(response.getNewPenalty());
			}
		} catch (DriverNotFoundException e) {
			log.warn(e.getMessage());
		}
	}

	private void processFlagResponse(FlagResponse response) {
		if (race.hasRaceEntry()) {
			Driver driver;
			try {
				driver = race.getRaceDriver(response);
				if (!race.hasFinished(driver)) {
					Lap lap = driver.getCurrentLap();
					Flag flag = lap.getCurrentFlag();
					if (flag != null) {
						if (response.getOn() && response.getFlag() == flag.getType()) {
							flag.setEndTime(System.currentTimeMillis());
						}
					}

					if (response.getOn()) {
						flag = new Flag();
						flag.setType(response.getFlag());
						flag.setStartTime(System.currentTimeMillis());
						lap.addFlag(flag);
					}
				}
			} catch (DriverNotFoundException e) {
				log.warn(e.getMessage());
			}
		}
	}

	public void exit() {
		try {
			client.close();
		} catch (IOException e) {
			log.error("Cannot close client connection", e);
		}
	}

	/*
	 * private void calculateQualifyingResults() { ArrayList<Result> allResults = new ArrayList<Result>();
	 * 
	 * for (Driver driver : race.getRaceDrivers()) { long bestLapTime = Long.MAX_VALUE; for (Lap lap : driver.getAllLaps()) { if (lap.isFinished() && bestLapTime > lap.getTime()) { bestLapTime =
	 * lap.getTime(); } } Result result = driver.getResult(); if (result == null) { result = new Result(); driver.setResult(result); } driver.getResult().setBestLapTime(bestLapTime);
	 * allResults.add(result); }
	 * 
	 * Collections.sort(allResults, new ResultComparator()); for (int i=0; i<allResults.size(); i++) { allResults.get(i).setPosition(i+1); log.debug(allResults.get(i)); } }
	 */

	/*
	 * @SuppressWarnings("unchecked") private void removeOldDriver(byte connectionId) { if (log.isDebugEnabled()) { log.debug("remove driver with connection id: " + connectionId); } Driver oldDriver =
	 * null; for (Driver driver : race.getDrivers()) { if (driver.getConnectionId() == connectionId) { oldDriver = driver; break; } } if (oldDriver != null) { if (log.isDebugEnabled()) {
	 * log.debug("removing driver: " + oldDriver); } race.removeDriver(oldDriver); } }
	 */

	public void run() {
		try {
			while (true) {
				Thread.sleep(10);
			}
		} catch (Exception ex) {
			log.error("Unexpected Exception", ex);
		}
	}

	private void processCommand(HiddenMessageResponse response) {
		String line = response.getMessage();
		log.debug("process command: " + line);
		if (line == null) {
			return;
		}

		if ("help".equalsIgnoreCase(line)) {
			log.info("Commands:");
			log.info("plugins - lists available plugins");
		} else if (line.startsWith("plugins")) {
			StringBuilder message = new StringBuilder("Available plugins: ");
			for (Plugin plugin : plugins) {
				message.append(plugin.getName());
				message.append(", ");
			}
			message.delete(message.length() - 2, message.length());
			ButtonMessageHelper.getInstance().sendButtonMessage(message.toString());
		}
	}

	public void setLapEventListener(List<LapEventListener> listener) {
		this.lapEventListener.addAll(listener);
	}

	public void addLapEventListener(LapEventListener listener) {
		lapEventListener.add(listener);
	}

	public void removeLapEventListener(LapEventListener listener) {
		lapEventListener.remove(listener);
	}

	public void notifyLapEventListener(LapEvent event) {
		for (LapEventListener listener : lapEventListener) {
			try {
				if (event.getSplit() > 0) {
					listener.lapSplit(event);
				} else {
					listener.lapFinished(event);
				}
			} catch (Exception ex) {
				log.error("Error notifying a lap event listener", ex);
			}
		}
	}

	private void notifyCameryEventListener(CameraEvent event) {
		for (CameraEventListener listener : cameraEventListener) {
			try {
				listener.cameraChangedEvent(event);
			} catch (Exception ex) {
				log.error("Error notifying a camera event listener", ex);
			}
		}

	}

	public void setRaceEventListener(List<RaceEventListener> listener) {
		this.raceEventListener.addAll(listener);
	}

	public void addRaceEventListener(RaceEventListener listener) {
		raceEventListener.add(listener);
	}

	public void removeRaceEventListener(RaceEventListener listener) {
		raceEventListener.remove(listener);
	}

	public void notifyRaceEventListener(RaceEvent event) {
		for (RaceEventListener listener : raceEventListener) {
			try {
				if (event.getType() == Type.STARTED) {
					listener.raceStartEvent(event);
				} else if (event.getType() == Type.NEW_DRIVER) {
					listener.raceNewDriverEvent(event);
				} else {
					listener.raceEndEvent(event);
				}
			} catch (Exception ex) {
				log.error("error notifying a race event listener", ex);
			}
		}
	}

	public void setPersistence(Persistence persistence) {
		this.persistence = persistence;
	}

	public void setClient(JInSimClient client) {
		this.client = client;
	}

	public Race getRace() {
		return race;
	}

	public void setPlugins(List<Plugin> plugins) {
		for (Plugin p : plugins) {
			this.plugins.add(p);
			client.addListener(p);
		}
	}

	public List<Plugin> getPlugins() {
		return plugins;
	}

}