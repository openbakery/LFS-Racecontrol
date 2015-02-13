package org.openbakery.racecontrol.control;

import org.openbakery.jinsim.response.ConnectionLeaveResponse;
import org.openbakery.jinsim.response.InSimResponse;
import org.openbakery.jinsim.response.NewConnectionResponse;
import org.openbakery.jinsim.response.NewPlayerResponse;
import org.openbakery.jinsim.response.TakeOverCarResponse;

import org.apache.commons.lang.StringUtils;
import org.openbakery.racecontrol.DriverNotFoundException;
import org.openbakery.racecontrol.Race;
import org.openbakery.racecontrol.RaceControl;
import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.RaceEntry;
import org.openbakery.racecontrol.event.RaceEvent;
import org.openbakery.racecontrol.event.RaceEvent.Type;
import org.openbakery.racecontrol.persistence.Persistence;
import org.openbakery.racecontrol.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class PlayerControl extends AbstractControl {

  private static Logger log = LoggerFactory.getLogger(PlayerControl.class);


	private HashMap<Integer, Driver> drivers;


	public PlayerControl(RaceControl raceControl, Persistence persistence) {
		super(raceControl, persistence);
		drivers = new HashMap<>();
	}

	public void packetReceived(InSimResponse response) {
		try {
			if (response instanceof NewConnectionResponse) {
				processNewConnectionResponse((NewConnectionResponse) response);
			} else if (response instanceof NewPlayerResponse) {
				processNewPlayerResponse((NewPlayerResponse) response);
			} else if (response instanceof ConnectionLeaveResponse) {
				removeOldDriver(((ConnectionLeaveResponse) response).getConnectionId());
			} else if (response instanceof TakeOverCarResponse) {
				processTakeOverCarResponse((TakeOverCarResponse) response);
			}
		} catch (PersistenceException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void processNewConnectionResponse(NewConnectionResponse response) {
		log.debug("new connection");
		removeOldDriver(response.getConnectionId());
		//Driver driver = raceControl.getRace().getDriver(response.getConnectionId(), response.getUsername());

		Driver driver = new Driver(response.getConnectionId());
		driver.setName(response.getUsername());
		driver.setPlayerName(response.getPlayerName());
		driver.setAdmin(response.isAdmin());
		drivers.put(driver.getConnectionId(), driver);
		log.debug(response.getPlayerName());
		log.debug("connected drivers {}", drivers);
	}

	private void processNewPlayerResponse(NewPlayerResponse response) throws PersistenceException {
		log.debug("process new player with connection id {}", response.getConnectionId());
		Race race = raceControl.getRace();

		Driver driver = race.getDriverByPlayerId(response.getPlayerId());

		if (driver != null && StringUtils.isNotEmpty(driver.getCarName()) && !response.getCar().toString().equals(driver.getCarName())) {
			// car has changed so create a new driver entry
			driver = null;
		}

		if (driver == null) {
			// driver has not joined the race yet so it is a new driver;
			log.debug("is new driver");
			driver = drivers.get(response.getConnectionId()); //race.getDriver(response.getConnectionId());

			//log.debug("driver ids: {}", drivers.keySet());
			//Driver d = drivers.values().iterator().next();
			//log.debug("{} == {}: {}", d.getConnectionId(), response.getConnectionId(), d.getConnectionId() == response.getConnectionId());

			try {
				driver = driver.clone();
			} catch (CloneNotSupportedException e) {
				log.debug("cannot clone driver {}", driver);
				return;
			}
		}

		log.debug("driver: {}", driver);
		if (race.hasFinished(driver)) {
			return;
		}

		driver.setData(response);
		if (driver.getName() == null) {
			log.debug("driver name is missing, therefor racecontrol connected during a race: " + driver);
			return;
		}
		race.addRaceDriver(driver);
		//Driver newDriver = persistence.store(driver);
		// race entry should be stored, so that the driver id is set properly
		RaceEntry raceEntry = persistence.store(race.getRaceEntry());
		Driver newDriver = raceEntry.getDriverWithName(driver.getName());
		driver.setId(newDriver.getId());
		driver.newLap();

		raceControl.notifyRaceEventListener(new RaceEvent(Type.NEW_DRIVER, race, driver));
		log.debug("new player: {}", driver);
	}



	protected void removeOldDriver(int connectionId) {
		if (log.isDebugEnabled()) {
			log.debug("remove driver with connection id: " + connectionId);
		}
		Driver oldDriver = raceControl.getRace().getDriver(connectionId);
		if (oldDriver != null) {
			if (log.isDebugEnabled()) {
				log.debug("removing driver: " + oldDriver);
			}
		}
	}

	private void processTakeOverCarResponse(TakeOverCarResponse response) {
		Driver driver;
		Race race = raceControl.getRace();
		try {
			driver = race.getRaceDriver(response);
			if (!race.hasFinished(driver)) {
				Driver newDriver = race.getDriver(response.getNewConnectionId());
				if (driver == newDriver) {
					driver.setCurrentDriver(null);
				} else if (newDriver != null) {
					driver.setCurrentDriver(newDriver);
				}
			}
		} catch (DriverNotFoundException e) {
			log.warn(e.getMessage());
		}
	}

	@Override
	public void destroy() {
	}

}
